package uk.gov.justice.laa.crime.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import uk.gov.justice.laa.crime.exception.ErrorExtension;
import uk.gov.justice.laa.crime.exception.ErrorMessage;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProblemDetailUtilTest {

    @Test
    void givenErrorList_whenErrorResponseWanted_ErrorListPresent(){
        ErrorExtension extension = createErrorExtension(2);
        ProblemDetail problemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), extension);
        assertThat(problemDetail).isNotNull();
        ErrorExtension foundErrors = (ErrorExtension) problemDetail.getProperties().get("errors");
        assertThat(foundErrors.getErrors()).isEqualTo(extension.getErrors());
    }

    // Test without using utility constructor. Verify getter behaviour.
    @Test
    void givenErrorListPresent_whenProblemDetailPassedIn_thenSameErrorListReturned(){
        ErrorExtension extension = createErrorExtension(2);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setProperties(Map.of("errors", extension));

        List<String> returnedErrors = ProblemDetailUtil.getErrorDetails(problemDetail);
        assertThat(returnedErrors).isEqualTo(getErrorsFromExtension(extension));
    }

    @Test
    void givenErrorList_whenProblemDetailCreatedAndThenPassedBack_thenSameErrorsPresent(){
        ErrorExtension extension = createErrorExtension(3);
        ProblemDetail problemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), extension);
        List<String> foundErrors = ProblemDetailUtil.getErrorDetails(problemDetail);
        assertThat(foundErrors).isEqualTo(getErrorsFromExtension(extension));
    }

    @Test
    void givenNoListButHasDetail_whenProblemDetailExamined_thenListOfDetailReturned(){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
        List<String> foundErrors = ProblemDetailUtil.getErrorDetails(detail);
        assertThat(foundErrors).isEqualTo(List.of(HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }

    @Test
    void givenNoDetailsOrList_whenProblemDetailsExamined_thenEmptyListReturned(){
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        List<String> foundErrors = ProblemDetailUtil.getErrorDetails(detail);
        assertThat(foundErrors).isEqualTo(List.of());
    }


    @Test
    void givenListAndDetail_whenGetListIsCalled_thenOnlyListReturned(){
        ErrorExtension extension = createErrorExtension(2);
        ProblemDetail problemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST, "detail", extension);

        List<String> foundErrors = ProblemDetailUtil.getErrorDetails(problemDetail);
        assertThat(foundErrors).isEqualTo(getErrorsFromExtension(extension));
    }

    @Test
    void givenListPresentButEmpty_whenProblemDetailPassedIn_thenListOfDetailReturned(){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Test Detail To Be Found");
        detail.setProperties(Map.of("errors", List.of()));
        List<String> foundErrors = ProblemDetailUtil.getErrorDetails(detail);
        assertThat(foundErrors).isEqualTo(List.of("Test Detail To Be Found"));
    }

    @Test
    void givenNoProblemDetail_whenNullPassedIn_thenEmptyListReturned(){
        List<String> foundErrors = ProblemDetailUtil.getErrorDetails(null);
        assertThat(foundErrors).isEqualTo(List.of());
    }

    @Test
    void givenAllDetails_whenBuildCalled_thenFullDetailReturned(){
        ErrorExtension expectedExtension = createErrorExtension(2);

        ProblemDetail problemDetail = ProblemDetailUtil.buildProblemDetail(HttpStatus.BAD_REQUEST, "title", "type", "detail", "instance", expectedExtension);

        assertThat(problemDetail).hasFieldOrPropertyWithValue("title", "title").hasFieldOrPropertyWithValue("type", URI.create("type"))
                .hasFieldOrPropertyWithValue("detail", "detail").hasFieldOrPropertyWithValue("instance", URI.create("instance"))
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST.value());

        Optional<ErrorExtension> foundExtension = ProblemDetailUtil.getErrorExtension(problemDetail);
        assertThat(foundExtension).isPresent();
        assertThat(foundExtension).contains(expectedExtension);
    }

    @Test
    void givenExtensionDetails_whenBuildExtensionCalled_populatesCorrectly(){
        ErrorExtension expectedExtension = createErrorExtension(2);

        ErrorExtension actualExtension = ProblemDetailUtil.buildErrorExtension(expectedExtension.getCode(), expectedExtension.getTraceId(), expectedExtension.getErrors());

        assertThat(actualExtension).isEqualTo(expectedExtension);
    }

    private List<ErrorMessage> createErrorMessages(int numValues){
        List<ErrorMessage> errors = new ArrayList<>();
        IntStream.range(0, numValues).forEach(i -> errors.add(new ErrorMessage("Field"+i, "Test Message"+i)));
        return errors;
    }

    private ErrorExtension createErrorExtension(int numValues){
        return new ErrorExtension("TestCode", "Test Trace ID", createErrorMessages(numValues));
    }

    private List<String> getErrorsFromExtension(ErrorExtension extension){
        return extension.getErrors().stream().map(ErrorMessage::getMessage).toList();
    }
}
