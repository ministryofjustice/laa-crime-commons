package uk.gov.justice.laa.crime.commons.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import uk.gov.justice.laa.crime.commons.common.Constants;
import uk.gov.justice.laa.crime.commons.exception.APIClientException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(MockitoExtension.class)
class RestAPIClientTest {

    public static final String MOCK_URL = "mock-url";
    public static final String MOCK_REGISTRATION_ID = "mock-web-client";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Map<String, String> MOCK_HEADERS = Map.of(Constants.LAA_TRANSACTION_ID, "laaTransactionId");
    private static final Map<String, Object> MOCK_GRAPHQL_BODY = Map.of("variables", "", "filter", "");
    public static final String MOCK_VALIDATION_ERROR = "Mock validation error";
    private final String MOCK_REP_ID = "1234";
    private RestAPIClient restAPIClient;
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

        restAPIClient = Mockito.spy(new RestAPIClient(testWebClient, MOCK_REGISTRATION_ID));
    }

    @Test
    void givenCorrectParams_whenPostIsInvoked_thenGetApiResponseIsCalled()
            throws JsonProcessingException {

        MockRequestBody request = new MockRequestBody();
        ParameterizedTypeReference<MockResponse> typeReference = new ParameterizedTypeReference<>() {
        };
        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        restAPIClient.post(request, typeReference, MOCK_URL, null);
        verify(restAPIClient)
                .getApiResponse(request, typeReference, MOCK_URL, null, HttpMethod.POST, null);
    }

    @Test
    void givenCorrectParams_whenPutIsInvoked_thenGetApiResponseIsCalled()
            throws JsonProcessingException {

        MockRequestBody request = new MockRequestBody();
        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        ParameterizedTypeReference<MockResponse> typeReference = new ParameterizedTypeReference<>() {
        };
        restAPIClient.put(request, typeReference, MOCK_URL, MOCK_HEADERS);

        verify(restAPIClient)
                .getApiResponse(request,
                        typeReference,
                        MOCK_URL,
                        MOCK_HEADERS,
                        HttpMethod.PUT,
                        null
                );
    }

    @Test
    void givenCorrectParams_whenHeadIsInvoked_thenGetBodilessApiResponseIsCalled()
            throws JsonProcessingException {

        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        restAPIClient.head(MOCK_URL, MOCK_HEADERS);

        verify(restAPIClient)
                .getBodilessApiResponse(
                        null,
                        MOCK_URL,
                        MOCK_HEADERS,
                        HttpMethod.HEAD,
                        null
                );
    }

    @Test
    void givenCorrectParams_whenGetIsInvoked_thenGetApiResponseIsCalled()
            throws JsonProcessingException {

        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        ParameterizedTypeReference<MockResponse> typeReference = new ParameterizedTypeReference<>() {
        };
        restAPIClient.get(typeReference, MOCK_URL, MOCK_REP_ID);

        verify(restAPIClient)
                .getApiResponse(
                        null,
                        typeReference, MOCK_URL,
                        null,
                        HttpMethod.GET,
                        null,
                        MOCK_REP_ID
                );
    }

    @Test
    void givenCorrectParams_whenGetIsInvokedWithHeaders_thenGetApiResponseIsCalled()
            throws JsonProcessingException {

        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        ParameterizedTypeReference<MockResponse> typeReference = new ParameterizedTypeReference<>() {
        };
        restAPIClient.get(typeReference, MOCK_URL, MOCK_HEADERS, MOCK_REP_ID);

        verify(restAPIClient)
                .getApiResponse(null,
                        typeReference,
                        MOCK_URL,
                        MOCK_HEADERS,
                        HttpMethod.GET,
                        null,
                        MOCK_REP_ID
                );
    }

    @Test
    void givenCorrectParams_whenGetIsInvokedWithQueryParams_thenGetApiResponseIsCalled()
            throws JsonProcessingException {

        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        MultiValueMapAdapter<String, String> queryParams =
                new MultiValueMapAdapter<>(Map.of("id", List.of("1000")));
        ParameterizedTypeReference<MockResponse> typeReference = new ParameterizedTypeReference<>() {
        };
        restAPIClient.get(typeReference, MOCK_URL, MOCK_HEADERS, queryParams);

        verify(restAPIClient)
                .getApiResponse(
                        null,
                        typeReference,
                        MOCK_URL,
                        MOCK_HEADERS,
                        HttpMethod.GET,
                        queryParams
                );
    }

    @Test
    void givenCorrectParams_whenGetApiResponseIsInvoked_thenCorrectResponseIsReturned()
            throws JsonProcessingException {

        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        MockResponse apiResponse = restAPIClient.getApiResponse(
                new MockRequestBody(),
                new ParameterizedTypeReference<>() {
                },
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
        setupBadRequestTest();
        assertThatThrownBy(
                () -> restAPIClient.getApiResponse(
                        new MockRequestBody(),
                        new ParameterizedTypeReference<MockResponse>() {
                        },
                        MOCK_URL,
                        MOCK_HEADERS,
                        HttpMethod.POST,
                        null
                )
        ).isInstanceOf(APIClientException.class)
                .hasMessage(String.format("Call to service %s failed.", MOCK_REGISTRATION_ID.toUpperCase()))
                .cause().isInstanceOf(WebClientResponseException.class);
    }

    @Test
    void givenAnInvalidUrl_whenGetApiResponseIsInvoked_thenReturnsNull() {
        setupNotFoundTest();
        MockResponse response = restAPIClient.getApiResponse(
                new MockRequestBody(),
                new ParameterizedTypeReference<>() {
                },
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
        MockResponse response = restAPIClient.getApiResponse(
                new MockRequestBody(),
                new ParameterizedTypeReference<>() {
                },
                MOCK_URL,
                MOCK_HEADERS,
                HttpMethod.GET,
                null,
                MOCK_REP_ID
        );
        assertThat(response).isNull();
    }

    @Test
    void givenParameterizedResponseSpec_whenGetApiResponseIsInvoked_thenResponseIsCorrectlyDeserialized()
            throws JsonProcessingException {

        List<MockResponse> expected =
                List.of(new MockResponse("1"), new MockResponse("2"));

        setupValidResponseTest(expected);
        List<MockResponse> response = restAPIClient.getApiResponse(
                new MockRequestBody(),
                new ParameterizedTypeReference<>() {
                },
                MOCK_URL,
                MOCK_HEADERS,
                HttpMethod.GET,
                null,
                MOCK_REP_ID
        );
        assertThat(response).isEqualTo(expected);
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

    private void setupBadRequestTest() {
        when(shortCircuitExchangeFunction.exchange(any()))
                .thenReturn(
                        Mono.just(ClientResponse
                                .create(HttpStatus.BAD_REQUEST)
                                .body("Required parameters missing")
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
                                    if (headers != null) {
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

    @Test
    void givenAValidInput_whenGetGraphQLApiResponseIsInvoked_thenResponseIsReturned()
            throws JsonProcessingException {
        MockResponse expected = new MockResponse("1");
        setupValidResponseTest(expected);
        MockResponse response = restAPIClient.getGraphQLApiResponse(
                MockResponse.class,
                MOCK_URL,
                MOCK_GRAPHQL_BODY
        );
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void givenInvalidData_whenGetGraphQLApiResponseIsInvoked_thenReturnsNull() {
        setupNotFoundTest();
        MockResponse response = restAPIClient.getGraphQLApiResponse(
                MockResponse.class,
                MOCK_URL,
                MOCK_GRAPHQL_BODY
        );
        assertThat(response).isNull();
    }

    record MockResponse(String id) {
    }

    record MockRequestBody() {
    }


    @Test
    void givenCorrectParams_whenPatchIsInvoked_thenGetApiResponseIsCalled()
            throws JsonProcessingException {

        MockRequestBody request = new MockRequestBody();
        setupValidResponseTest(new MockResponse(MOCK_REP_ID));
        ParameterizedTypeReference<MockResponse> typeReference = new ParameterizedTypeReference<>() {
        };
        restAPIClient.patch(request, typeReference, MOCK_URL, MOCK_HEADERS);

        verify(restAPIClient)
                .getApiResponse(request,
                        typeReference,
                        MOCK_URL,
                        MOCK_HEADERS,
                        HttpMethod.PATCH,
                        null
                );
    }

}
