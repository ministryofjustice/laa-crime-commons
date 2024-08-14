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
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import uk.gov.justice.laa.crime.commons.common.ErrorDTO;
import uk.gov.justice.laa.crime.commons.config.RetryConfiguration;
import uk.gov.justice.laa.crime.commons.exception.APIClientException;
import uk.gov.justice.laa.crime.commons.exception.RetryableWebClientResponseException;
import uk.gov.justice.laa.crime.commons.exception.MAATApplicationException;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * WebClientFilters defines several ExchangeFilterFunctions used to:
 * <ul>
 * <li>Log requests headers</li>
 * <li>Log response status codes</li>
 * <li>Handle error responses based on the status code</li>
 * <li>Define a retry strategy for handling potentially recoverable errors</li>
 * </ul>
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WebClientFilters {

    /**
     * Filters client requests
     * Logs all non-sensitive client request headers at INFO level
     *
     * @return the exchange filter function
     */
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


    /**
     * Filters client responses
     * Logs the response status code at INFO level
     *
     * @return the exchange filter function
     */
    public static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    /**
     * Handles error response by logging the status code and response phrase, then throwing an appropriate exception:
     * <ul>
     * <li><code>RetryableWebClientResponseException</code> is thrown on potentially recoverable status codes</li>
     * <li><code>HttpClientErrorException</code> is thrown on client error status codes (excl. 404 NOT FOUND)</li>
     * <li><code>HttpServerErrorException</code> is thrown on server error status codes</li>
     * <li><code>WebClientResponseException</code> is thrown for all remaining error status codes</li>
     * </ul>
     *
     * @return the exchange filter function
     * @see RetryableWebClientResponseException
     */
    public static ExchangeFilterFunction handleErrorResponse() {

        List<HttpStatus> retryableStatuses = List.of(
                HttpStatus.REQUEST_TIMEOUT, HttpStatus.TOO_EARLY, HttpStatus.TOO_MANY_REQUESTS, HttpStatus.BAD_GATEWAY,
                HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.GATEWAY_TIMEOUT
        );

        return ExchangeFilterFunction.ofResponseProcessor(
                response -> {
                    HttpStatus status = HttpStatus.valueOf(response.statusCode().value());

                    String errorMessage = String.format("Received error %s due to %s",
                            response.statusCode().value(), status.getReasonPhrase());

                    if (retryableStatuses.contains(status)) {
                        return Mono.error(new RetryableWebClientResponseException(errorMessage));
                    }

                    if (status.is4xxClientError()) {
                        if (status.equals(HttpStatus.NOT_FOUND)) {
                            return Mono.error(WebClientResponseException.create(response.statusCode().value(), status.getReasonPhrase(), null, null, null));
                        }
                        return Mono.error(new HttpClientErrorException(response.statusCode(), errorMessage));
                    }

                    if (status.is5xxServerError()) {
                        Optional<Mono<ErrorDTO>> errorDTOMono = Optional.ofNullable(response.bodyToMono(ErrorDTO.class));
                        if (errorDTOMono.isPresent() && HttpStatus.INTERNAL_SERVER_ERROR == response.statusCode()) {
                            return errorDTOMono.get()
                                    .flatMap(errorBody -> Mono.error(new MAATApplicationException(errorBody.getMessage())));
                        }
                        return Mono.error(new HttpServerErrorException(response.statusCode(), errorMessage));
                    }
                    return Mono.just(response);
                });
    }

    /**
     * Retry exchange filter function.
     * Retries error responses on <code>RetryableWebClientResponseException</code> and request timeouts
     * Progress is logged to the console after each retry and after all retries are exhausted at INFO level
     * The number of retries, jitter and back of period are provided via the <code>retryConfiguration</code> argument
     *
     * @param retryConfiguration the retry configuration
     * @return the exchange filter function
     * @see RetryConfiguration
     */
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
