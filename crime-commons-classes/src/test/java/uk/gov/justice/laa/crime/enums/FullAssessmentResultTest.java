package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class FullAssessmentResultTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(FullAssessmentResult.getFrom(null)).isNull();
    }

    @Test
    void givenAValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(FullAssessmentResult.FAIL).isEqualTo(FullAssessmentResult.getFrom("FAIL"));
    }

    @Test
    void valueOfCurrentStatusFromString_valueNotFound_throwsException() {
        assertThatThrownBy(
                () -> FullAssessmentResult.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("INEL").isEqualTo(FullAssessmentResult.INEL.getResult());
        assertThat("Refused - Ineligible").isEqualTo(FullAssessmentResult.INEL.getReason());
    }

}