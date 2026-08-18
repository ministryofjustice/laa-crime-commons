package uk.gov.justice.laa.crime.enums;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AssessmentResultTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(AssessmentResult.getFrom(null)).isNull();
    }

    @Test
    void givenAnInvalidValue_whenGetFromIsInvoked_thenExceptionIsThrown() {
        assertThatThrownBy(
            () -> AssessmentResult.getFrom("MISSING VALUE")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidValues_whenGetFromIsInvoked_ThenCorrectValuesReturned() {
        assertThat("PASS").isEqualTo(AssessmentResult.PASS.getResult());
        assertThat("Gross income below the lower threshold").isEqualTo(AssessmentResult.PASS.getReason());
        assertThat("FAIL").isEqualTo(AssessmentResult.FAIL.getResult());
        assertThat("Gross income above the upper threshold").isEqualTo(AssessmentResult.FAIL.getReason());
        assertThat("HARDSHIP APPLICATION").isEqualTo(AssessmentResult.HARDSHIP.getResult());
        assertThat("Hardship application").isEqualTo(AssessmentResult.HARDSHIP.getReason());
        assertThat("INEL").isEqualTo(AssessmentResult.INEL.getResult());
        assertThat("Refused - Ineligible").isEqualTo(AssessmentResult.INEL.getReason());
        assertThat("FULL").isEqualTo(AssessmentResult.FULL.getResult());
        assertThat("Gross income in between the upper and lower thresholds").isEqualTo(AssessmentResult.FULL.getReason());
        assertThat("TEMP").isEqualTo(AssessmentResult.TEMP.getResult());
        assertThat("Temporary pass for a Passported application").isEqualTo(AssessmentResult.TEMP.getReason());
        assertThat("FAIL CONTINUE").isEqualTo(AssessmentResult.FAIL_CONTINUE.getResult());
        assertThat("A Benefit bypass for a Passported application").isEqualTo(AssessmentResult.FAIL_CONTINUE.getReason());
    }

}
