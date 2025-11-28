package uk.gov.justice.laa.crime.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProblemDetailErrorListUtilTest {

    @Test
    void givenErrorList_whenErrorResponseWanted_ErrorListPresent(){
        List<String> errorList = List.of("Error 1", "Error 2");
        ProblemDetail problemDetail = ProblemDetailErrorListUtil.buildProblemDetail(errorList, HttpStatus.BAD_REQUEST);
        assertThat(problemDetail).isNotNull();
        List<String> foundErrors = (List<String>) problemDetail.getProperties().get("messageList");
        assertThat(foundErrors).isEqualTo(errorList);
    }

    @Test
    void givenErrorListPresent_whenProblemDetailPassedIn_thenSameErrorListReturned(){
        List<String> errorList = List.of("Error 1", "Error 2");
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setProperties(Map.of("messageList", errorList));

        List<String> returnedErrors = ProblemDetailErrorListUtil.getErrorMessages(problemDetail);
        assertThat(returnedErrors).isEqualTo(errorList);
    }

    @Test
    void givenErrorList_whenProblemDetailCreatedAndThenPassedBack_thenSameErrorsPresent(){
        List<String> errorList = List.of("Error 1", "Error 2");
        ProblemDetail problemDetail = ProblemDetailErrorListUtil.buildProblemDetail(errorList, HttpStatus.BAD_REQUEST);
        List<String> foundErrors = ProblemDetailErrorListUtil.getErrorMessages(problemDetail);
        assertThat(foundErrors).isEqualTo(errorList);
    }

    @Test
    void givenNoListButHasDetail_whenProblemDetailExamined_thenListOfDetailReturned(){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Test Detail To Be Found");
        List<String> foundErrors = ProblemDetailErrorListUtil.getErrorMessages(detail);
        assertThat(foundErrors).isEqualTo(List.of("Test Detail To Be Found"));
    }

    @Test
    void givenNoDetailsOrList_whenProblemDetailsExamined_thenEmptyListReturned(){
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        List<String> foundErrors = ProblemDetailErrorListUtil.getErrorMessages(detail);
        assertThat(foundErrors).isEqualTo(List.of());
    }

    @Test
    void givenProblemDetailAndList_whenAddToProblemDetailIsCalled_thenListIsAdded(){
        List<String> errorList = List.of("Error 1", "Error 2");
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        ProblemDetailErrorListUtil.addMessageList(errorList, problemDetail);

        List<String> foundErrors = ProblemDetailErrorListUtil.getErrorMessages(problemDetail);
        assertThat(foundErrors).isEqualTo(errorList);
    }

    @Test
    void givenListAndDetail_whenGetListIsCalled_thenOnlyListReturned(){
        List<String> errorList = List.of("Error 1", "Error 2");
        ProblemDetail problemDetail = ProblemDetailErrorListUtil.buildProblemDetail(errorList, HttpStatus.BAD_REQUEST, "detail");

        List<String> foundErrors = ProblemDetailErrorListUtil.getErrorMessages(problemDetail);
        assertThat(foundErrors).isEqualTo(errorList);
    }

    @Test
    void givenListPresentButEmpty_whenProblemDetailPassedIn_thenListOfDetailReturned(){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Test Detail To Be Found");
        detail.setProperties(Map.of("messageList", List.of()));
        List<String> foundErrors = ProblemDetailErrorListUtil.getErrorMessages(detail);
        assertThat(foundErrors).isEqualTo(List.of("Test Detail To Be Found"));
    }

    @Test
    void givenNoProblemDetail_whenNullPassedIn_thenEmptyListReturned(){
        List<String> foundErrors = ProblemDetailErrorListUtil.getErrorMessages(null);
        assertThat(foundErrors).isEqualTo(List.of());
    }
}
