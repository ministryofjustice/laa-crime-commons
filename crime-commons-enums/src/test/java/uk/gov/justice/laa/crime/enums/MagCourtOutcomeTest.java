package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MagCourtOutcomeTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(MagCourtOutcome.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(MagCourtOutcome.getFrom("COMMITTED FOR TRIAL")).isEqualTo(MagCourtOutcome.COMMITTED_FOR_TRIAL);
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThatThrownBy(
                () -> MagCourtOutcome.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("COMMITTED FOR TRIAL").isEqualTo(MagCourtOutcome.COMMITTED_FOR_TRIAL.getOutcome());
        assertThat("Committed for Trial").isEqualTo(MagCourtOutcome.COMMITTED_FOR_TRIAL.getDescription());
    }

}