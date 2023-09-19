package uk.gov.justice.laa.crime.commons.filters;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.netty.handler.timeout.ReadTimeoutException;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import uk.gov.justice.laa.crime.commons.common.Constants;
import uk.gov.justice.laa.crime.commons.config.RetryConfiguration;
import uk.gov.justice.laa.crime.commons.exception.APIClientException;
import uk.gov.justice.laa.crime.commons.exception.RetryableWebClientResponseException;
import uk.gov.justice.laa.crime.commons.util.MemoryAppender;

import java.net.URI;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SoftAssertionsExtension.class)
class WebClientFiltersTest {

    @Mock
    private ClientResponse clientResponse;

    @Mock
    private ExchangeFunction exchangeFunction;

    @InjectSoftAssertions
    private SoftAssertions softly;


    private static MemoryAppender memoryAppender;
    private static final URI DEFAULT_URL = URI.create("https://example.com");
    private static final String TRANSACTION_ID = UUID.randomUUID().toString();
    private static final String LOGGER_NAME = "uk.gov.justice.laa.crime.commons.filters";

    private final RetryConfiguration retryConfiguration =
            new RetryConfiguration(2, 5, 0.75);

    @BeforeEach
    public void setup() {
        memoryAppender = new MemoryAppender();
        Logger logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);
        logger.setLevel(Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        memoryAppender.start();
    }

    @AfterEach
    public void cleanUp() {
        memoryAppender.reset();
        memoryAppender.stop();
    }

    @Test
    void givenClientRequest_whenLogRequestHeadersIsInvoked_thenCorrectHeadersAreLogged() {

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.put(Constants.LAA_TRANSACTION_ID, TRANSACTION_ID);
        headers.put("Authorization", "Bearer X5N9-0R23CR9-230R9C2M");

        ClientRequest request = ClientRequest.create(HttpMethod.GET, DEFAULT_URL)
                .headers(httpHeaders -> httpHeaders.setAll(headers))
                .build();

        doReturn(Mono.just(clientResponse))
                .when(exchangeFunction).exchange(request);

        WebClientFilters.logRequestHeaders().filter(request, exchangeFunction);

        softly.assertThat(memoryAppender.countEventsForLogger(LOGGER_NAME))
                .isEqualTo(3);

        softly.assertThat(memoryAppender.contains(
                CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE, Level.INFO)
        ).isTrue();

        softly.assertThat(memoryAppender.contains(
                Constants.LAA_TRANSACTION_ID + "=" + TRANSACTION_ID, Level.INFO)
        ).isTrue();

        softly.assertThat(memoryAppender.contains(
                "Authorization", Level.INFO)
        ).isFalse();

        softly.assertAll();
    }

    @Test
    void givenSuccessResponse_whenLogResponseIsInvoked_thenResponseStatusIsLogged() {
        ClientRequest request = ClientRequest.create(HttpMethod.GET, DEFAULT_URL).build();

        doReturn(Mono.just(clientResponse))
                .when(exchangeFunction).exchange(request);

        when(clientResponse.statusCode())
                .thenReturn(HttpStatus.OK);

        Mono<ClientResponse> response =
                WebClientFilters.logResponse().filter(request, exchangeFunction);
        response.block();

        assertThat(memoryAppender.contains("Response status: 200 OK", Level.INFO))
                .isTrue();
    }

    @Test
    void givenNotFoundStatus_whenHandleErrorResponseIsInvoked_thenWebClientResponseExceptionIsThrown() {
        ClientRequest request = ClientRequest.create(HttpMethod.GET, DEFAULT_URL).build();
        doReturn(Mono.just(clientResponse))
                .when(exchangeFunction).exchange(request);

        when(clientResponse.statusCode())
                .thenReturn(HttpStatus.NOT_FOUND);

        Mono<ClientResponse> response = WebClientFilters.handleErrorResponse()
                .filter(request, exchangeFunction);

        assertThatThrownBy(
                response::block
        ).isInstanceOf(WebClientResponseException.class)
                .hasMessage("404 Not Found");
    }

    @Test
    void givenClientErrorStatus_whenHandleErrorResponseIsInvoked_thenHttpClientErrorExceptionIsThrown() {
        ClientRequest request = ClientRequest.create(HttpMethod.GET, DEFAULT_URL).build();
        doReturn(Mono.just(clientResponse))
                .when(exchangeFunction).exchange(request);

        when(clientResponse.statusCode())
                .thenReturn(HttpStatus.FORBIDDEN);

        Mono<ClientResponse> response = WebClientFilters.handleErrorResponse()
                .filter(request, exchangeFunction);

        assertThatThrownBy(
                response::block
        ).isInstanceOf(HttpClientErrorException.class)
                .hasMessage("403 Received error 403 due to Forbidden");
    }

    @Test
    void givenServerErrorStatus_whenHandleErrorResponseIsInvoked_thenHttpServerErrorExceptionIsThrown() {
        ClientRequest request = ClientRequest.create(HttpMethod.GET, DEFAULT_URL).build();
        doReturn(Mono.just(clientResponse))
                .when(exchangeFunction).exchange(request);

        when(clientResponse.statusCode())
                .thenReturn(HttpStatus.NOT_IMPLEMENTED);

        Mono<ClientResponse> response = WebClientFilters.handleErrorResponse()
                .filter(request, exchangeFunction);

        assertThatThrownBy(
                response::block
        ).isInstanceOf(HttpServerErrorException.class)
                .hasMessage("501 Received error 501 due to Not Implemented");
    }

    @Test
    void givenRetryableErrorStatus_whenHandleErrorResponseIsInvoked_thenRetryableWebClientResponseExceptionIsThrown() {
        ClientRequest request = ClientRequest.create(HttpMethod.GET, DEFAULT_URL).build();
        doReturn(Mono.just(clientResponse))
                .when(exchangeFunction).exchange(request);

        when(clientResponse.statusCode())
                .thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        Mono<ClientResponse> response = WebClientFilters.handleErrorResponse()
                .filter(request, exchangeFunction);

        assertThatThrownBy(
                response::block
        ).isInstanceOf(RetryableWebClientResponseException.class)
                .hasMessage("Received error 500 due to Internal Server Error");
    }

    @Test
    void givenBadRequestStatus_whenHandleErrorResponseIsInvoked_thenHttpClientErrorExceptionIsThrown() {
        ClientRequest request = ClientRequest.create(HttpMethod.GET, DEFAULT_URL).build();
        doReturn(Mono.just(clientResponse))
                .when(exchangeFunction).exchange(request);

        when(clientResponse.statusCode())
                .thenReturn(HttpStatus.BAD_REQUEST);

        Mono<ClientResponse> response = WebClientFilters.handleErrorResponse()
                .filter(request, exchangeFunction);

        assertThatThrownBy(
                response::block
        ).isInstanceOf(HttpClientErrorException.class)
                .hasMessage("400 Received error 400 due to Bad Request");
    }

    @Test
    void givenRetriesExhausted_whenRetryFilterIsInvoked_thenAPIClientExceptionIsThrown() {

        ClientRequest request = ClientRequest.create(HttpMethod.GET, DEFAULT_URL).build();

        RetryableWebClientResponseException retryableException = new RetryableWebClientResponseException();
        WebClientRequestException timeoutException = new WebClientRequestException(
                new ReadTimeoutException(), HttpMethod.GET, DEFAULT_URL, new HttpHeaders()
        );

        LinkedList<Throwable> errors = new LinkedList<>(
                Arrays.asList(retryableException, timeoutException, retryableException)
        );

        Mono<ClientResponse> errorMono = Mono.error(errors::pop);
        when(exchangeFunction.exchange(request))
                .thenReturn(errorMono);

        Mono<ClientResponse> response =
                WebClientFilters.retryFilter(retryConfiguration).filter(request, exchangeFunction);

        assertThatThrownBy(
                response::block
        ).isInstanceOf(APIClientException.class)
                .hasMessageContaining("Call to service failed. Retries exhausted: 2/2");
    }

    @Test
    void givenRetriesNotExhausted_whenRetryFilterIsInvoked_thenOKResponseIsReceived() {

        ClientRequest request = ClientRequest.create(HttpMethod.GET, DEFAULT_URL).build();

        RetryableWebClientResponseException retryableException = new RetryableWebClientResponseException();
        WebClientRequestException timeoutException = new WebClientRequestException(
                new ReadTimeoutException(), HttpMethod.GET, DEFAULT_URL, new HttpHeaders()
        );

        LinkedList<RuntimeException> errors = new LinkedList<>(
                Arrays.asList(retryableException, timeoutException)
        );

        Mono<ClientResponse> responseMono = Mono.fromCallable(
                () -> {
                    if (errors.isEmpty()) {
                        return ClientResponse.create(HttpStatus.OK).build();
                    }
                    throw errors.pop();
                }
        );

        when(exchangeFunction.exchange(request))
                .thenReturn(responseMono);

        Mono<ClientResponse> clientResponse =
                WebClientFilters.retryFilter(retryConfiguration).filter(request, exchangeFunction);

        ClientResponse response = clientResponse.block();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void givenUnRetryableException_whenRetryFilterIsInvoked_thenCorrectExceptionIsThrown() {

        ClientRequest request = ClientRequest.create(HttpMethod.GET, DEFAULT_URL).build();

        WebClientRequestException unauthorizedException = new WebClientRequestException(
                new HttpClientErrorException(HttpStatus.UNAUTHORIZED), HttpMethod.GET, DEFAULT_URL, new HttpHeaders()
        );

        LinkedList<Throwable> errors = new LinkedList<>(
                List.of(unauthorizedException)
        );

        Mono<ClientResponse> errorMono = Mono.error(errors::pop);
        when(exchangeFunction.exchange(request))
                .thenReturn(errorMono);

        Mono<ClientResponse> response =
                WebClientFilters.retryFilter(retryConfiguration).filter(request, exchangeFunction);

        assertThatThrownBy(
                response::block
        ).isInstanceOf(WebClientRequestException.class)
                .hasMessageContaining("401 UNAUTHORIZED");
    }
}
