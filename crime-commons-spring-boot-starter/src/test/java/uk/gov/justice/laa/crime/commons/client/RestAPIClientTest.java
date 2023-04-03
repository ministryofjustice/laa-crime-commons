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
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import uk.gov.justice.laa.crime.commons.common.Constants;
import uk.gov.justice.laa.crime.commons.exception.APIClientException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(MockitoExtension.class)
class RestAPIClientTest {

    private final Integer REP_ID = 1234;
    private RestAPIClient restAPIClient;
    public static final String MOCK_URL = "mock-url";
    private final String MOCK_TRANSACTION_ID = "laaTransactionId";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

    record MockResponse(String status) {}

    @Test
    void givenApiClientException_whenHandleErrorIsInvoked_thenExistingErrorIsReturned() {
        String mockResponse = "MOCK ERROR RESPONSE";
        APIClientException mockException = new APIClientException(mockResponse);
        assertThat(restAPIClient.handleError(mockException))
                .isInstanceOf(APIClientException.class).hasMessage(mockResponse);
    }

    @Test
    void givenAnInvalidResponse_whenGetApiResponseIsInvoked_thenAnAppropriateErrorShouldBeThrown() {
        setupInvalidResponseTest();
        assertThatThrownBy(
                () -> restAPIClient.getApiResponse(
                        new Object(),
                        ClientResponse.class,
                        MOCK_URL,
                        Map.of(Constants.LAA_TRANSACTION_ID, MOCK_TRANSACTION_ID),
                        HttpMethod.POST
                )
        ).isInstanceOf(APIClientException.class).cause().isInstanceOf(WebClientResponseException.class);
    }

    @Test
    void givenANotFoundException_whenGetApiResponseViaGetIsInvoked_thenTheMethodShouldReturnNull() {
        setupNotFoundTest();
        ClientResponse response = restAPIClient.getApiResponseViaGET(
                ClientResponse.class,
                MOCK_URL,
                Map.of(Constants.LAA_TRANSACTION_ID, MOCK_TRANSACTION_ID),
                REP_ID
        );
        assertThat(response).isNull();
    }

    @Test
    void givenAnInvalidResponse_whenGetApiResponseViaGetIsInvoked_thenAnAppropriateErrorShouldBeThrown() {
        setupInvalidResponseTest();
        assertThatThrownBy(
                () -> restAPIClient.getApiResponseViaGET(
                        ClientResponse.class,
                        MOCK_URL,
                        Map.of(Constants.LAA_TRANSACTION_ID, MOCK_TRANSACTION_ID),
                        REP_ID
                )
        ).isInstanceOf(APIClientException.class).cause().isInstanceOf(WebClientResponseException.class);
    }

    @Test
    void givenCorrectParams_whenGetApiResponseViaPOSTIsInvoked_thenGetApiResponseIsCalledWithCorrectMethod()
            throws JsonProcessingException {

        MockResponse response = new MockResponse("mock-status");
        setupValidResponseTest(response);
        restAPIClient.getApiResponseViaPOST(
                response,
                MockResponse.class,
                MOCK_URL,
                null
        );
        verify(restAPIClient)
                .getApiResponse(
                        response,
                        MockResponse.class,
                        MOCK_URL,
                        null,
                        HttpMethod.POST
                );
    }

    @Test
    void givenCorrectParams_whenGetApiResponseViaPUTIsInvoked_thenGetApiResponseIsCalledWithCorrectMethod()
            throws JsonProcessingException {

        MockResponse response = new MockResponse("mock-status");
        setupValidResponseTest(response);
        restAPIClient.getApiResponseViaPUT(
                response,
                MockResponse.class,
                MOCK_URL,
                Map.of(Constants.LAA_TRANSACTION_ID, MOCK_TRANSACTION_ID)
        );

        verify(restAPIClient)
                .getApiResponse(
                        response,
                        MockResponse.class,
                        MOCK_URL,
                        Map.of(Constants.LAA_TRANSACTION_ID, MOCK_TRANSACTION_ID),
                        HttpMethod.PUT
                );
    }

    @Test
    void givenAnInvalidUrl_whenGetApiResponseViaGetIsInvoked_thenTheMethodShouldReturnNull() {
        setupNotFoundTest();
        ClientResponse response = restAPIClient.getApiResponseViaGET(
                ClientResponse.class,
                MOCK_URL,
                REP_ID
        );
        assertThat(response).isNull();
    }

    @Test
    void givenAnInvalidUrl_whenGetGetApiResponseIsInvoked_thenTheMethodShouldReturnNull() {
        setupNotFoundTest();
        ClientResponse response = restAPIClient.getApiResponse(
                new Object(),
                ClientResponse.class,
                MOCK_URL,
                Map.of(Constants.LAA_TRANSACTION_ID, MOCK_TRANSACTION_ID),
                HttpMethod.POST
        );
        assertThat(response).isNull();
    }

    @Test
    void givenCorrectParams_whenGetApiResponseViaGETIsInvoked_thenGetApiResponseIsCalledWithCorrectMethod()
            throws JsonProcessingException {

        MockResponse response = new MockResponse("mock-status");
        setupValidResponseTest(response);
        MockResponse apiResponse = restAPIClient.getApiResponseViaGET(
                MockResponse.class,
                MOCK_URL,
                Map.of(Constants.LAA_TRANSACTION_ID, MOCK_TRANSACTION_ID),
                1234
        );
        verify(shortCircuitExchangeFunction).exchange(any());
        assertThat(apiResponse.status).isEqualTo(response.status);
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

    private <T> void setupValidResponseTest(T returnBody) throws JsonProcessingException {
        String body = OBJECT_MAPPER.writeValueAsString(returnBody);
        when(shortCircuitExchangeFunction.exchange(any()))
                .thenReturn(
                        Mono.just(ClientResponse
                                .create(HttpStatus.OK)
                                .body(body)
                                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .build()
                        )
                );
    }
}
