package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class InitAssessmentResultTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(InitAssessmentResult.getFrom(null)).isNull();
    }

    @Test
    void givenAValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(InitAssessmentResult.FAIL).isEqualTo(InitAssessmentResult.getFrom("FAIL"));
    }

    @Test
    void valueOfCurrentStatusFromString_valueNotFound_throwsException() {
        assertThatThrownBy(
                () -> InitAssessmentResult.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("FULL").isEqualTo(InitAssessmentResult.FULL.getResult());
        assertThat("Gross income in between the upper and lower thresholds").isEqualTo(InitAssessmentResult.FULL.getReason());
    }

}