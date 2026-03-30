package uk.gov.justice.laa.crime.util;


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
import uk.gov.justice.laa.crime.error.ProblemDetailError;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemDetailUtilTest {

    private static final String CODE = "code";
    private static final String TRACE_ID = "trace-123";
    private static final String TEST_DETAIL = "TestDetail";

    @Test
    void givenAllDetails_whenBuildCalled_thenFullDetailReturned() {

        String title = "title";
        String type = "type";
        String instance = "instance";

        ErrorExtension expectedExtension = createErrorExtension(2);
        ProblemDetail problemDetail =
                ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST, title, type,
                        TEST_DETAIL, instance, expectedExtension);

        assertThat(problemDetail.getTitle()).isEqualTo(title);
        assertThat(problemDetail.getType()).isEqualTo(URI.create(type));
        assertThat(problemDetail.getDetail()).isEqualTo(TEST_DETAIL);
        assertThat(problemDetail.getInstance()).isEqualTo(URI.create(instance));
        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        Optional<ErrorExtension> foundExtension = ProblemDetailUtil.getErrorExtension(
                problemDetail);

        assertThat(foundExtension)
                .isPresent()
                .contains(expectedExtension);
    }

    @Test
    void givenProblemDetailError_whenBuildProblemDetailCalled_thenUsesDefaultDetailAndCode() {
        List<ErrorMessage> messages = List.of(new ErrorMessage("field", "message"));

        ProblemDetail problemDetail = ProblemDetailUtil.buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                ProblemDetailError.VALIDATION_FAILURE,
                TRACE_ID,
                messages);

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getDetail())
                .isEqualTo(ProblemDetailError.VALIDATION_FAILURE.defaultDetail());

        assertThat(ProblemDetailUtil.getErrorExtension(problemDetail))
                .contains(new ErrorExtension(
                        ProblemDetailError.VALIDATION_FAILURE.code(),
                        TRACE_ID,
                        messages));
    }

    @Test
    void givenExtensionDetails_whenBuildExtensionCalled_populatesCorrectly() {
        ErrorExtension expectedExtension = createErrorExtension(2);

        ErrorExtension actualExtension = ProblemDetailUtil.buildErrorExtension(
                expectedExtension.code(), expectedExtension.traceId(), expectedExtension.errors());

        assertThat(actualExtension).isEqualTo(expectedExtension);
    }

    @Test
    void givenProblemDetailWithDetailOnly_whenGetErrorDetailsWithDefault_thenReturnsDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase());

        List<String> returnedErrors = ProblemDetailUtil.getErrorDetailsWithDefault(problemDetail);

        assertThat(returnedErrors).containsExactly(HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @Test
    void givenProblemDetailWithDetailOnly_whenGetErrorDetails_thenReturnsEmptyList() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase());

        List<String> returnedErrors = ProblemDetailUtil.getErrorDetails(problemDetail);

        assertThat(returnedErrors).isEmpty();
    }

    // Test without using a utility constructor. Verify getter behaviour.
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
    void givenNoDetailsOrList_whenProblemDetailsExamined_thenEmptyListReturned(boolean useDefault) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        List<String> returnedErrors = (useDefault) ?
                ProblemDetailUtil.getErrorDetailsWithDefault(problemDetail) :
                ProblemDetailUtil.getErrorDetails(problemDetail);
        assertThat(returnedErrors).isEmpty();
    }


    @Test
    void givenListAndDetail_whenGetListIsCalled_thenOnlyListReturned() {
        ErrorExtension extension = createErrorExtension(2);
        ProblemDetail problemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST,
                TEST_DETAIL, extension);

        List<String> returnedErrors = ProblemDetailUtil.getErrorDetailsWithDefault(problemDetail);
        assertThat(returnedErrors).isEqualTo(getErrorsFromExtension(extension));
    }

    @Test
    void givenListPresentButEmpty_whenProblemDetailPassedIn_thenListOfDetailReturned() {
        ErrorExtension extension = new ErrorExtension(CODE, TRACE_ID, List.of());
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, TEST_DETAIL);
        detail.setProperties(Map.of(ErrorExtension.EXTENSION_KEY, extension));
        List<String> returnedErrors = ProblemDetailUtil.getErrorDetailsWithDefault(detail);
        assertThat(returnedErrors).isEqualTo(List.of(TEST_DETAIL));
    }

    @Test
    void givenNoProblemDetail_whenNullPassedIn_thenEmptyListReturned() {
        List<String> returnedErrors = ProblemDetailUtil.getErrorDetailsWithDefault(null);
        assertThat(returnedErrors).isEmpty();
    }

    @Test
    void givenProblemDetailWithExtension_whenGetErrorExtension_thenReturnsExtension() {
        ErrorExtension extension = createErrorExtension(2);
        ProblemDetail problemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(), extension);
        assertThat(ProblemDetailUtil.getErrorExtension(problemDetail))
                .contains(extension);
    }

    /**
     * Incoming JSON can vary depending on whether the responding endpoint uses the
     * {@link ProblemDetailJacksonMixin}. When it does, values in {@code properties}
     * are flattened to the top level.
     *
     * <p>For example:
     * <pre>
     * {
     *   "type": "whatever",
     *   "errors": {
     *     "code": "TestCode"
     *   }
     * }
     * </pre>
     *
     * Both forms should be supported.
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenProblemDetail_whenParseIsCalled_populatesCorrectly(boolean useMixins) throws JsonProcessingException {
        ErrorExtension expectedExtension = createErrorExtension(2);
        ProblemDetail expectedProblemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST, TEST_DETAIL, expectedExtension);
        ObjectMapper objectMapper = new ObjectMapper();
        if (useMixins) {
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
        ProblemDetail expectedProblemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST, TEST_DETAIL, null);
        ObjectMapper objectMapper = new ObjectMapper();
        if (useMixins) {
            objectMapper.addMixIn(ProblemDetail.class, ProblemDetailJacksonMixin.class);
        }

        String json = objectMapper.writeValueAsString(expectedProblemDetail);

        ProblemDetail actualProblemDetail = ProblemDetailUtil.parseProblemDetailJson(json);

        assertThat(actualProblemDetail).isEqualTo(expectedProblemDetail);
        assertThat(ProblemDetailUtil.getErrorExtension(actualProblemDetail)).isEmpty();
    }

    @Test
    void givenNoExtensions_whenParseIsCalled_populatesCorrectly() throws JsonProcessingException {
        ProblemDetail expectedProblemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, TEST_DETAIL);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(expectedProblemDetail);

        ProblemDetail actualProblemDetail = ProblemDetailUtil.parseProblemDetailJson(json);

        assertThat(actualProblemDetail).isEqualTo(expectedProblemDetail);
        assertThat(ProblemDetailUtil.getErrorExtension(actualProblemDetail)).isEmpty();
    }

    @Test
    void givenProblemDetailWithUnsupportedExtensionType_whenGetErrorExtension_thenReturnsEmpty() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperties(Map.of(ErrorExtension.EXTENSION_KEY, "not-an-extension"));

        assertThat(ProblemDetailUtil.getErrorExtension(problemDetail)).isEmpty();
    }

    private List<ErrorMessage> createErrorMessages(int numValues) {
        List<ErrorMessage> errors = new ArrayList<>();
        IntStream.range(0, numValues)
                .forEach(i -> errors.add(new ErrorMessage("Field" + i, "Test Message" + i)));
        return errors;
    }

    private ErrorExtension createErrorExtension(int numValues) {
        return new ErrorExtension(CODE, TRACE_ID, createErrorMessages(numValues));
    }

    private List<String> getErrorsFromExtension(ErrorExtension extension) {
        return extension.errors().stream().map(ErrorMessage::message).toList();
    }
}
