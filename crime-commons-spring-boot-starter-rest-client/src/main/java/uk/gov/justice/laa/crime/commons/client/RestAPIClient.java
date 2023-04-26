package uk.gov.justice.laa.crime.commons.client;

import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import uk.gov.justice.laa.crime.commons.config.RestClientAutoConfiguration;
import uk.gov.justice.laa.crime.commons.exception.APIClientException;

import java.net.URI;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestAPIClient {

    private final WebClient webClient;
    private final String registrationId;

    public ResponseEntity<Void> head(String url, Map<String, String> headers, Object... urlVariables) {
        return getBodilessApiResponse(null, url, headers, HttpMethod.HEAD, null, urlVariables);
    }

    public <T> T get(Class<T> responseClass,
                     String url,
                     Map<String, String> headers,
                     MultiValueMap<String, String> queryParams,
                     Object... urlVariables) {
        return getApiResponse(null, responseClass, url, headers, HttpMethod.GET, queryParams, urlVariables);
    }

    public <T> T get(Class<T> responseClass, String url, Map<String, String> headers, Object... urlVariables) {
        return getApiResponse(null, responseClass, url, headers, HttpMethod.GET, null, urlVariables);
    }

    public <T> T get(Class<T> responseClass, String url, Object... urlVariables) {
        return getApiResponse(null, responseClass, url, null, HttpMethod.GET, null, urlVariables);
    }

    public <T, R> R post(T requestBody, Class<R> responseClass, String url, Map<String, String> headers) {
        return getApiResponse(requestBody, responseClass, url, headers, HttpMethod.POST, null);
    }

    public <T, R> R put(T requestBody, Class<R> responseClass, String url, Map<String, String> headers) {
        return getApiResponse(requestBody, responseClass, url, headers, HttpMethod.PUT, null);
    }

    private <T> WebClient.RequestHeadersSpec<?> prepareRequest(T requestBody,
                                                               String url,
                                                               Map<String, String> headers,
                                                               HttpMethod requestMethod,
                                                               MultiValueMap<String, String> queryParams,
                                                               Object... urlVariables) {

        URI path = UriComponentsBuilder.fromUriString(url).queryParams(queryParams).build(urlVariables);

        BodyInserter<T, ReactiveHttpOutputMessage> bodyInserters =
                (requestBody != null) ? BodyInserters.fromValue(requestBody) : BodyInserters.empty();

        return webClient
                .method(requestMethod)
                .uri(path)
                .headers(httpHeaders -> {
                    if (headers != null) {
                        httpHeaders.setAll(headers);
                    }
                })
                .body(bodyInserters)
                .attributes(RestClientAutoConfiguration.getExchangeFilterWith(registrationId));
    }

    private <T> Mono<T> configureErrorResponse(Mono<T> mono) {
        return mono
                .onErrorResume(WebClientResponseException.NotFound.class, notFound -> Mono.empty())
                .onErrorMap(this::handleError)
                .doOnError(Sentry::captureException);
    }

    <T, R> R getApiResponse(T requestBody,
                            Class<R> responseClass,
                            String url,
                            Map<String, String> headers,
                            HttpMethod requestMethod,
                            MultiValueMap<String, String> queryParams,
                            Object... urlVariables) {

        WebClient.RequestHeadersSpec<?> requestHeadersSpec =
                prepareRequest(requestBody, url, headers, requestMethod, queryParams, urlVariables);
        return configureErrorResponse(requestHeadersSpec.retrieve().bodyToMono(responseClass)).block();
    }

    <T> ResponseEntity<Void> getBodilessApiResponse(T requestBody,
                                                    String url,
                                                    Map<String, String> headers,
                                                    HttpMethod requestMethod,
                                                    MultiValueMap<String, String> queryParams,
                                                    Object... urlVariables) {

        WebClient.RequestHeadersSpec<?> requestHeadersSpec =
                prepareRequest(requestBody, url, headers, requestMethod, queryParams, urlVariables);
        return configureErrorResponse(requestHeadersSpec.retrieve().toBodilessEntity()).block();
    }

    private Throwable handleError(Throwable error) {
        if (error instanceof APIClientException) {
            return error;
        }
        String serviceName = registrationId.toUpperCase();
        return new APIClientException(String.format("Call to service %s failed.", serviceName), error);
    }
}
