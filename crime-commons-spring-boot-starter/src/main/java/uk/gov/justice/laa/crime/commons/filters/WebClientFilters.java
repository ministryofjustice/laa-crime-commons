package uk.gov.justice.laa.crime.commons.filters;

import io.netty.handler.timeout.TimeoutException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import uk.gov.justice.laa.crime.commons.config.RetryConfiguration;
import uk.gov.justice.laa.crime.commons.exception.APIClientException;
import uk.gov.justice.laa.crime.commons.exception.RetryableWebClientResponseException;

import java.time.Duration;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WebClientFilters {

    public static ExchangeFilterFunction logRequestHeaders() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> {
                        if (!name.equals("Authorization")) {
                            values.forEach(value -> log.info("{}={}", name, value));
                        }
                    });
            return next.exchange(clientRequest);
        };
    }


    public static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    public static ExchangeFilterFunction handleErrorResponse() {

        List<HttpStatus> retryableStatuses = List.of(
                HttpStatus.REQUEST_TIMEOUT, HttpStatus.TOO_EARLY, HttpStatus.TOO_MANY_REQUESTS, HttpStatus.BAD_GATEWAY,
                HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.GATEWAY_TIMEOUT
        );

        return ExchangeFilterFunctions.statusError(
                HttpStatusCode::isError, r -> {

                    HttpStatus status = HttpStatus.valueOf(r.statusCode().value());

                    String errorMessage =
                            String.format("Received error %s due to %s",
                                    r.statusCode().value(), status.getReasonPhrase());

                    if (retryableStatuses.contains(status)) {
                        return new RetryableWebClientResponseException(errorMessage);
                    }

                    if (status.is4xxClientError() && !status.equals(HttpStatus.NOT_FOUND)) {
                        return new HttpClientErrorException(r.statusCode(), errorMessage);
                    }

                    if (status.is5xxServerError()) {
                        return new HttpServerErrorException(r.statusCode(), errorMessage);
                    }
                    return WebClientResponseException.create(
                            r.statusCode().value(), status.getReasonPhrase(), null, null, null
                    );
                });
    }

    public static ExchangeFilterFunction retryFilter(RetryConfiguration retryConfiguration) {
        return (request, next) ->
                next.exchange(request)
                        .retryWhen(
                                Retry.backoff(
                                                retryConfiguration.getMaxRetries(),
                                                Duration.ofSeconds(
                                                        retryConfiguration.getMinBackOffPeriod()
                                                )
                                        )
                                        .jitter(retryConfiguration.getJitterValue())
                                        .filter(
                                                throwable ->
                                                        throwable instanceof RetryableWebClientResponseException ||
                                                                throwable instanceof WebClientRequestException
                                                                        && throwable.getCause() instanceof TimeoutException
                                        ).onRetryExhaustedThrow(
                                                (retryBackoffSpec, retrySignal) ->
                                                        new APIClientException(
                                                                String.format(
                                                                        "Call to service failed. Retries exhausted: %d/%d.",
                                                                        retrySignal.totalRetries(), retryConfiguration.getMaxRetries()
                                                                ), retrySignal.failure()
                                                        )
                                        ).doAfterRetry(
                                                retrySignal -> {
                                                    if (retrySignal.totalRetries() > 0) {
                                                        log.warn(
                                                                String.format("Call to service failed, retrying: %d/%d",
                                                                        retrySignal.totalRetries(), retryConfiguration.getMaxRetries()
                                                                )
                                                        );
                                                    }
                                                }
                                        )
                        );
    }
}
