package uk.gov.justice.laa.crime.commons.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import uk.gov.justice.laa.crime.commons.common.Constants;
import uk.gov.justice.laa.crime.commons.exception.APIClientException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(MockitoExtension.class)
class RestAPIClientTest {

    private final String MOCK_REP_ID = "1234";
    private RestAPIClient restAPIClient;
    public static final String MOCK_URL = "mock-url";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Map<String, String> MOCK_HEADERS = Map.of(Constants.LAA_TRANSACTION_ID, "laaTransactionId");

    @Mock
    private ExchangeFunction shortCircuitExchangeFunction;

    @BeforeEach
    void setup() {
        WebClient testWebClient = WebClient
                .builder()
                .baseUrl("http://localhost:1234")
                .filter(ExchangeFilterFunctions.statusError(
                                HttpStatusCode::is4xxClientError,
                                r -> {
                                    HttpStatus status = HttpStatus.valueOf(r.statusCode().value());
                                    return WebClientResponseException.create(
                                            status.value(),
                                            status.getReasonPhrase(),
                                            null,
                                            null,
                                            null
                                    );
                                }
                        )
                )
                .exchangeFunction(shortCircuitExchangeFunction)
                .build();

        restAPIClient = Mockito.spy(new RestAPIClient(testWebClient, "mock-web-client"));
    }

    record MockResponse(String id) {
    }

    record MockRequestBody() {
    }

    @Test
    void givenApiClientException_whenHandleErrorIsInvoked_thenExistingErrorIsReturned() {
        String mockResponse = "MOCK ERROR RESPONSE";
        APIClientException mockException = new APIClientException(mockResponse);
        assertThat(restAPIClient.handleError(mockException))
                .isInstanceOf(APIClientException.class).hasMessage(mockResponse);
    }

    @Test
    void givenCorrectParams_whenPostIsInvoked_thenGetApiResponseIsCalled()
            throws JsonProcessingException {

        MockRequestBody request = new MockRequestBody();
        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        restAPIClient.post(request, MockResponse.class, MOCK_URL, null);
        verify(restAPIClient)
                .getApiResponse(request, MockResponse.class, MOCK_URL, null, HttpMethod.POST, null);
    }

    @Test
    void givenCorrectParams_whenPutIsInvoked_thenGetApiResponseIsCalled()
            throws JsonProcessingException {

        MockRequestBody request = new MockRequestBody();
        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        restAPIClient.put(request, MockResponse.class, MOCK_URL, MOCK_HEADERS);

        verify(restAPIClient)
                .getApiResponse(request, MockResponse.class, MOCK_URL, MOCK_HEADERS, HttpMethod.PUT, null);
    }

    @Test
    void givenCorrectParams_whenHeadIsInvoked_thenGetBodilessApiResponseIsCalled()
            throws JsonProcessingException {

        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        restAPIClient.head(MOCK_URL, MOCK_HEADERS);

        verify(restAPIClient)
                .getBodilessApiResponse(null, MOCK_URL, MOCK_HEADERS, HttpMethod.HEAD, null);
    }

    @Test
    void givenCorrectParams_whenGetIsInvoked_thenGetApiResponseIsCalled()
            throws JsonProcessingException {


        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        restAPIClient.get(MockResponse.class, MOCK_URL, MOCK_REP_ID);

        verify(restAPIClient)
                .getApiResponse(null, MockResponse.class, MOCK_URL, null, HttpMethod.GET, null, MOCK_REP_ID);
    }

    @Test
    void givenCorrectParams_whenGetIsInvokedWithHeaders_thenGetApiResponseIsCalled()
            throws JsonProcessingException {

        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        restAPIClient.get(MockResponse.class, MOCK_URL, MOCK_HEADERS, MOCK_REP_ID);

        verify(restAPIClient)
                .getApiResponse(null, MockResponse.class, MOCK_URL, MOCK_HEADERS, HttpMethod.GET, null, MOCK_REP_ID);
    }

    @Test
    void givenCorrectParams_whenGetIsInvokedWithQueryParams_thenGetApiResponseIsCalled()
            throws JsonProcessingException {

        MultiValueMapAdapter<String, String> queryParams =
                new MultiValueMapAdapter<>(Map.of("id", List.of("1000")));

        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        restAPIClient.get(MockResponse.class, MOCK_URL, MOCK_HEADERS, queryParams);

        verify(restAPIClient)
                .getApiResponse(null, MockResponse.class, MOCK_URL, MOCK_HEADERS, HttpMethod.GET, queryParams);
    }

    @Test
    void givenCorrectParams_whenGetApiResponseIsInvoked_thenCorrectResponseIsReturned()
            throws JsonProcessingException {

        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        MockResponse apiResponse = restAPIClient.getApiResponse(
                new MockRequestBody(),
                MockResponse.class,
                MOCK_URL,
                MOCK_HEADERS,
                HttpMethod.POST,
                null
        );
        assertThat(apiResponse.id).isEqualTo(MOCK_REP_ID);
    }

    @Test
    void givenCorrectParams_whenGetBodilessApiResponseIsInvoked_thenCorrectResponseIsReturned()
            throws JsonProcessingException {

        setupValidResponseTest(new MockResponse(MOCK_REP_ID), Map.of(CONTENT_LENGTH, "10"));
        ResponseEntity<Void> apiResponse = restAPIClient.getBodilessApiResponse(
                new MockRequestBody(),
                MOCK_URL,
                MOCK_HEADERS,
                HttpMethod.HEAD,
                null
        );
        assertThat(apiResponse.getHeaders().getContentLength()).isEqualTo(10);
    }

    @Test
    void givenAnInvalidResponse_whenGetApiResponseIsInvoked_thenErrorIsThrown() {
        setupInvalidResponseTest();
        assertThatThrownBy(
                () -> restAPIClient.getApiResponse(
                        new MockRequestBody(),
                        ClientResponse.class,
                        MOCK_URL,
                        MOCK_HEADERS,
                        HttpMethod.POST,
                        null
                )
        ).isInstanceOf(APIClientException.class).cause().isInstanceOf(WebClientResponseException.class);
    }

    @Test
    void givenAnInvalidUrl_whenGetApiResponseIsInvoked_thenReturnsNull() {
        setupNotFoundTest();
        ClientResponse response = restAPIClient.getApiResponse(
                new MockRequestBody(),
                ClientResponse.class,
                MOCK_URL,
                MOCK_HEADERS,
                HttpMethod.POST,
                null
        );
        assertThat(response).isNull();
    }

    @Test
    void givenANotFoundException_whenGetApiResponseIsInvoked_thenReturnsNull() {
        setupNotFoundTest();
        ClientResponse response = restAPIClient.getApiResponse(
                new MockRequestBody(),
                ClientResponse.class,
                MOCK_URL,
                MOCK_HEADERS,
                HttpMethod.GET,
                null,
                MOCK_REP_ID
        );
        assertThat(response).isNull();
    }

    private void setupNotFoundTest() {
        when(shortCircuitExchangeFunction.exchange(any()))
                .thenReturn(
                        Mono.just(ClientResponse
                                .create(HttpStatus.NOT_FOUND)
                                .body("Error")
                                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .build()
                        )
                );
    }

    private void setupInvalidResponseTest() {
        when(shortCircuitExchangeFunction.exchange(any()))
                .thenReturn(
                        Mono.just(ClientResponse
                                .create(HttpStatus.OK)
                                .body("Invalid response")
                                .build()
                        )
                );
    }

    private <T> void setupValidResponseTest(T returnBody, Map<String, String> headers) throws JsonProcessingException {
        String body = OBJECT_MAPPER.writeValueAsString(returnBody);
        when(shortCircuitExchangeFunction.exchange(any()))
                .thenReturn(
                        Mono.just(ClientResponse
                                .create(HttpStatus.OK)
                                .body(body)
                                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .headers(httpHeaders -> {
                                    if(headers != null) {
                                        httpHeaders.setAll(headers);
                                    }
                                })
                                .build()
                        )
                );
    }

    private <T> void setupValidResponseTest(T returnBody) throws JsonProcessingException {
        setupValidResponseTest(returnBody, null);
    }
}
