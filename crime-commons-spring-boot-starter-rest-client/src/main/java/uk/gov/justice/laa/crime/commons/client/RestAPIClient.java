package uk.gov.justice.laa.crime.commons.client;

import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
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

/**
 * RestApiClient is designed to simplify sending API request to LAA microservices
 * Requests are delegated to a Spring reactive WebClient, with OAuth2 enabled by default
 * Credentials are specified as application properties and retrieved via the associated registrationId
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RestAPIClient {

    private final WebClient webClient;
    private final String registrationId;

    /**
     * Sends a HTTP HEAD request
     *
     * @param url          the url
     * @param headers      the map of headers
     * @param urlVariables the map of url variables
     * @return the response entity (without a body)
     */
    public ResponseEntity<Void> head(String url, Map<String, String> headers, Object... urlVariables) {
        return getBodilessApiResponse(null, url, headers, HttpMethod.HEAD, null, urlVariables);
    }

    /**
     * Sends a HTTP GET request
     *
     * @param <R>          the return type
     * @param url          the url
     * @param headers      the map of headers
     * @param queryParams  the map of query parameters
     * @param urlVariables the map of url variables
     * @param typeReference specifies the class/type used for deserialization
     * @return the decoded response body
     */
    public <R> R get(ParameterizedTypeReference<R> typeReference,
                     String url,
                     Map<String, String> headers,
                     MultiValueMap<String, String> queryParams,
                     Object... urlVariables) {
        return getApiResponse(null, typeReference, url, headers, HttpMethod.GET, queryParams, urlVariables);
    }

    /**
     * Sends a HTTP GET request
     *
     * @param <R>          the return type
     * @param typeReference specifies the class/type used for deserialization
     * @param url          the url
     * @param headers      the map of headers
     * @param urlVariables the map of url variables
     * @return the decoded response body
     */
    public <R> R get(ParameterizedTypeReference<R> typeReference, String url, Map<String, String> headers, Object... urlVariables) {
        return getApiResponse(null, typeReference, url, headers, HttpMethod.GET, null, urlVariables);
    }

    /**
     * Sends a HTTP GET request
     *
     * @param <R>          the return type
     * @param typeReference specifies the class/type used for deserialization
     * @param url          the url
     * @param urlVariables the map of url variables
     * @return the decoded response body
     */
    public <R> R get(ParameterizedTypeReference<R> typeReference, String url, Object... urlVariables) {
        return getApiResponse(null, typeReference, url, null, HttpMethod.GET, null, urlVariables);
    }

    /**
     * Sends a HTTP POST request
     *
     * @param <T>          the type of the request body
     * @param <R>          the return type
     * @param requestBody  the request body
     * @param typeReference specifies the class/type used for deserialization
     * @param url          the url
     * @param headers      the map of headers
     * @return the decoded response body
     */
    public <T, R> R post(T requestBody, ParameterizedTypeReference<R> typeReference, String url, Map<String, String> headers) {
        return getApiResponse(requestBody, typeReference, url, headers, HttpMethod.POST, null);
    }

    /**
     * Sends a HTTP PUT request
     *
     * @param <T>          the type of the request body
     * @param <R>          the return type
     * @param requestBody  the request body
     * @param typeReference specifies the class/type used for deserialization
     * @param url          the url
     * @param headers      the map of headers
     * @return the decoded response body
     */
    public <T, R> R put(T requestBody, ParameterizedTypeReference<R> typeReference, String url, Map<String, String> headers) {
        return getApiResponse(requestBody, typeReference, url, headers, HttpMethod.PUT, null);
    }

    /**
     * Sends a HTTP PUT request
     *
     * @param <T>          the type of the request body
     * @param <R>          the return type
     * @param requestBody  the request body
     * @param typeReference specifies the class/type used for deserialization
     * @param url          the url
     * @param headers      the map of headers
     * @param urlVariables the path variables
     * @return the decoded response body
     */
    public <T, R> R put(T requestBody, ParameterizedTypeReference<R> typeReference, String url, Map<String, String> headers, Object... urlVariables) {
        return getApiResponse(requestBody, typeReference, url, headers, HttpMethod.PUT, null, urlVariables);
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

    /**
     * Sends an API request and returns the decoded response body
     *
     * @param <T>           the type of the request body
     * @param <R>           the return type
     * @param requestBody   the request body
     * @param typeReference  specifies the class/type used for deserialization
     * @param url           the url
     * @param headers       the map of headers
     * @param requestMethod the HTTP request method
     * @param queryParams   the map of query parameters
     * @param urlVariables  the map of url variables
     * @return the decoded api response body
     */
    <T, R> R getApiResponse(T requestBody,
                            ParameterizedTypeReference<R> typeReference,
                            String url,
                            Map<String, String> headers,
                            HttpMethod requestMethod,
                            MultiValueMap<String, String> queryParams,
                            Object... urlVariables) {

        WebClient.RequestHeadersSpec<?> requestHeadersSpec =
                prepareRequest(requestBody, url, headers, requestMethod, queryParams, urlVariables);
        return configureErrorResponse(requestHeadersSpec.retrieve().bodyToMono(typeReference)).block();
    }

    /**
     * Sends an API request and returns the response entity (without a body)
     *
     * @param <T>           the type of the request body
     * @param requestBody   the request body
     * @param url           the url
     * @param headers       the map of headers
     * @param requestMethod the HTTP request method
     * @param queryParams   the map of query parameters
     * @param urlVariables  the map of url variables
     * @return the response entity (without a body)
     */
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

    public <R> R getGraphQLApiResponse(Class<R> responseClass,
                                       String url,
                                       Map<String, Object> graphQLBody) {
        return webClient
                .post()
                .uri(url)
                .bodyValue(graphQLBody)
                .retrieve()
                .bodyToMono(responseClass)
                .onErrorResume(WebClientResponseException.NotFound.class, notFound -> Mono.empty())
                .onErrorMap(this::handleError)
                .doOnError(Sentry::captureException)
                .block();
    }


    /**
     * Sends a HTTP PATCH request
     *
     * @param <T>          the type of the request body
     * @param <R>          the return type
     * @param requestBody  the request body
     * @param typeReference specifies the class/type used for deserialization
     * @param url          the url
     * @param headers      the map of headers
     * @return the decoded response body
     */
    public <T, R> R patch(T requestBody, ParameterizedTypeReference<R> typeReference, String url, Map<String, String> headers, Object... urlVariables) {
        return getApiResponse(requestBody, typeReference, url, headers, HttpMethod.PATCH, null, urlVariables);
    }
}
