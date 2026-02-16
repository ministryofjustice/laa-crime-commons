package uk.gov.justice.laa.crime.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.json.ProblemDetailJacksonMixin;
import uk.gov.justice.laa.crime.error.ErrorExtension;
import uk.gov.justice.laa.crime.error.ErrorMessage;

class ProblemDetailUtilTest {

    @Test
    void givenErrorList_whenErrorResponseWanted_ErrorListPresent() {
        ErrorExtension extension = createErrorExtension(2);
        ProblemDetail problemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(), extension);
        assertThat(problemDetail).isNotNull();
        assertThat(ProblemDetailUtil.getErrorExtension(problemDetail))
                .contains(extension);
    }

    // Test without using utility constructor. Verify getter behaviour.
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenErrorListPresent_whenProblemDetailPassedIn_thenSameErrorListReturned(boolean useDefault) {
        ErrorExtension extension = createErrorExtension(2);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setProperties(Map.of(ErrorExtension.EXTENSION_KEY, extension));
        List<String> returnedErrors = (useDefault) ?
                ProblemDetailUtil.getErrorDetailsWithDefault(problemDetail) :
                ProblemDetailUtil.getErrorDetails(problemDetail);
        assertThat(returnedErrors).isEqualTo(getErrorsFromExtension(extension));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenErrorList_whenProblemDetailCreatedAndThenPassedBack_thenSameErrorsPresent(boolean useDefault) {
        ErrorExtension extension = createErrorExtension(3);
        ProblemDetail problemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(), extension);
        List<String> returnedErrors = (useDefault) ?
                ProblemDetailUtil.getErrorDetailsWithDefault(problemDetail) :
                ProblemDetailUtil.getErrorDetails(problemDetail);
        assertThat(returnedErrors).isEqualTo(getErrorsFromExtension(extension));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenNoListButHasDetail_whenProblemDetailExamined_thenListOfDetailReturned(boolean useDefault) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase());
        if(useDefault) {
            List<String> returnedErrors = ProblemDetailUtil.getErrorDetailsWithDefault(problemDetail);
            assertThat(returnedErrors).isEqualTo(List.of(HttpStatus.BAD_REQUEST.getReasonPhrase()));
        }
        else{
            List<String> returnedErrors = ProblemDetailUtil.getErrorDetails(problemDetail);
            assertThat(returnedErrors).isEqualTo(Collections.emptyList());
        }

    }


    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenNoDetailsOrList_whenProblemDetailsExamined_thenEmptyListReturned(boolean useDefault) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        List<String> returnedErrors = (useDefault) ?
                ProblemDetailUtil.getErrorDetailsWithDefault(problemDetail) :
                ProblemDetailUtil.getErrorDetails(problemDetail);
        assertThat(returnedErrors).isEqualTo(List.of());
    }


    @Test
    void givenListAndDetail_whenGetListIsCalled_thenOnlyListReturned() {
        ErrorExtension extension = createErrorExtension(2);
        ProblemDetail problemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST,
                "detail", extension);

        List<String> returnedErrors = ProblemDetailUtil.getErrorDetailsWithDefault(problemDetail);
        assertThat(returnedErrors).isEqualTo(getErrorsFromExtension(extension));
    }

    @Test
    void givenListPresentButEmpty_whenProblemDetailPassedIn_thenListOfDetailReturned() {
        ErrorExtension extension = new ErrorExtension("code", "trace", List.of());
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Test Detail To Be Found");
        detail.setProperties(Map.of(ErrorExtension.EXTENSION_KEY, extension));
        List<String> returnedErrors = ProblemDetailUtil.getErrorDetailsWithDefault(detail);
        assertThat(returnedErrors).isEqualTo(List.of("Test Detail To Be Found"));
    }

    @Test
    void givenNoProblemDetail_whenNullPassedIn_thenEmptyListReturned() {
        List<String> returnedErrors = ProblemDetailUtil.getErrorDetailsWithDefault(null);
        assertThat(returnedErrors).isEqualTo(List.of());
    }

    @Test
    void givenAllDetails_whenBuildCalled_thenFullDetailReturned() {

        ErrorExtension expectedExtension = createErrorExtension(2);
        ProblemDetail problemDetail =
                ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST, "title", "type",
                        "detail", "instance", expectedExtension);

        assertThat(problemDetail.getTitle()).isEqualTo("title");
        assertThat(problemDetail.getType()).isEqualTo(URI.create("type"));
        assertThat(problemDetail.getDetail()).isEqualTo("detail");
        assertThat(problemDetail.getInstance()).isEqualTo(URI.create("instance"));
        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        Optional<ErrorExtension> foundExtension = ProblemDetailUtil.getErrorExtension(
                problemDetail);
        assertThat(foundExtension).isPresent();
        assertThat(foundExtension).contains(expectedExtension);
    }

    @Test
    void givenExtensionDetails_whenBuildExtensionCalled_populatesCorrectly() {
        ErrorExtension expectedExtension = createErrorExtension(2);

        ErrorExtension actualExtension = ProblemDetailUtil.buildErrorExtension(
                expectedExtension.code(), expectedExtension.traceId(), expectedExtension.errors());

        assertThat(actualExtension).isEqualTo(expectedExtension);
    }


    /**
     * Format of incoming json can be different if the endpoint responding is using the jackson mixin correctly.
     * If using, the values in properties get moved to the top level.
     * i.e.
     * {
     *  "type" : "whatever"
     *  "errors" : {
     *      "code" : "TestCode"
     *  }
     * }
     * Need to be able to handle both versions.
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenProblemDetail_whenParseIsCalled_populatesCorrectly(boolean useMixins) throws JsonProcessingException {
        ErrorExtension expectedExtension = createErrorExtension(2);
        ProblemDetail  expectedProblemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST, "Detail", expectedExtension);
        ObjectMapper objectMapper = new ObjectMapper();
        if(useMixins) {
            objectMapper.addMixIn(ProblemDetail.class, ProblemDetailJacksonMixin.class);
        }
        String json = objectMapper.writeValueAsString(expectedProblemDetail);

        ProblemDetail actualProblemDetail = ProblemDetailUtil.parseProblemDetailJson(json);

        assertThat(actualProblemDetail).isEqualTo(expectedProblemDetail);
        assertThat(ProblemDetailUtil.getErrorExtension(actualProblemDetail)).contains(expectedExtension);
    }


    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenProblemDetailWithNoExtension_whenParseIsCalled_populatesCorrectly(boolean useMixins) throws JsonProcessingException {
        ProblemDetail  expectedProblemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST, "Detail", null);
        ObjectMapper objectMapper = new ObjectMapper();
        if(useMixins) {
            objectMapper.addMixIn(ProblemDetail.class, ProblemDetailJacksonMixin.class);
        }

        String json = objectMapper.writeValueAsString(expectedProblemDetail);

        ProblemDetail actualProblemDetail = ProblemDetailUtil.parseProblemDetailJson(json);

        assertThat(actualProblemDetail).isEqualTo(expectedProblemDetail);
        assertFalse(ProblemDetailUtil.getErrorExtension(actualProblemDetail).isPresent());
    }

    @Test
    void givenNoExtensions_whenParseIsCalled_populatesCorrectly() throws JsonProcessingException {
        ProblemDetail  expectedProblemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "TestDetail");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(expectedProblemDetail);

        ProblemDetail actualProblemDetail = ProblemDetailUtil.parseProblemDetailJson(json);

        assertThat(actualProblemDetail).isEqualTo(expectedProblemDetail);
        assertFalse(ProblemDetailUtil.getErrorExtension(actualProblemDetail).isPresent());

    }

    private List<ErrorMessage> createErrorMessages(int numValues) {
        List<ErrorMessage> errors = new ArrayList<>();
        IntStream.range(0, numValues)
                .forEach(i -> errors.add(new ErrorMessage("Field" + i, "Test Message" + i)));
        return errors;
    }

    private ErrorExtension createErrorExtension(int numValues) {
        return new ErrorExtension("TestCode", "Test Trace ID", createErrorMessages(numValues));
    }

    private List<String> getErrorsFromExtension(ErrorExtension extension) {
        return extension.errors().stream().map(ErrorMessage::message).toList();
    }
}
