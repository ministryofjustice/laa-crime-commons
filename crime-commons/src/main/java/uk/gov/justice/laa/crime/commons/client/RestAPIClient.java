package uk.gov.justice.laa.crime.commons.client;

import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import uk.gov.justice.laa.crime.commons.config.APIClientAutoConfiguration;
import uk.gov.justice.laa.crime.commons.exception.APIClientException;

import java.net.URI;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestAPIClient {

    private final WebClient webClient;
    private final String registrationId;

    public <T> T getApiResponseViaGET(Class<T> responseClass,
                                      String url, Map<String, String> headers,
                                      MultiValueMap<String, String> queryParams,
                                      Object... urlVariables) {

        URI path = UriComponentsBuilder.fromUriString(url)
                .queryParams(queryParams)
                .build(urlVariables);

        return webClient
                .get()
                .uri(path)
                .headers(httpHeaders -> {
                    if (headers != null) {
                        httpHeaders.setAll(headers);
                    }
                })
                .attributes(APIClientAutoConfiguration.getExchangeFilterWith(registrationId))
                .retrieve()
                .bodyToMono(responseClass)
                .onErrorResume(WebClientResponseException.NotFound.class, notFound -> Mono.empty())
                .onErrorMap(this::handleError)
                .doOnError(Sentry::captureException)
                .block();
    }

    public <T> T getApiResponseViaGET(Class<T> responseClass, String url, Map<String, String> headers, Object... urlVariables) {
        return getApiResponseViaGET(responseClass, url, headers, null, urlVariables);
    }

    public <T> T getApiResponseViaGET(Class<T> responseClass, String url, Object... urlVariables) {
        return getApiResponseViaGET(responseClass, url, null, null, urlVariables);
    }

    public <T, R> R getApiResponseViaPOST(T requestBody, Class<R> responseClass, String url, Map<String, String> headers) {
        return getApiResponse(requestBody, responseClass, url, headers, HttpMethod.POST);
    }

    public <T, R> R getApiResponseViaPUT(T requestBody, Class<R> responseClass, String url, Map<String, String> headers) {
        return getApiResponse(requestBody, responseClass, url, headers, HttpMethod.PUT);
    }

    <T, R> R getApiResponse(T requestBody,
                            Class<R> responseClass,
                            String url, Map<String, String> headers,
                            HttpMethod requestMethod) {

        return webClient
                .method(requestMethod)
                .uri(url)
                .headers(httpHeaders -> {
                    if (headers != null) {
                        httpHeaders.setAll(headers);
                    }
                })
                .attributes(APIClientAutoConfiguration.getExchangeFilterWith(registrationId))
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(responseClass)
                .onErrorResume(WebClientResponseException.NotFound.class, notFound -> Mono.empty())
                .onErrorMap(this::handleError)
                .doOnError(Sentry::captureException)
                .block();
    }

    Throwable handleError(Throwable error) {
        if (error instanceof APIClientException) {
            return error;
        }
        String serviceName = registrationId.toUpperCase();
        return new APIClientException(String.format("Call to service %s failed.", serviceName), error);
    }
}
