package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CrownCourtOutcomeTest {

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(CrownCourtOutcome.getFrom("ABANDONED")).isEqualTo(CrownCourtOutcome.ABANDONED);
    }

    @Test
    void givenBlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(CrownCourtOutcome.getFrom(null)).isNull();
    }

    @Test
    void givenInvalidResultString_whenGetFromIsInvoked_thenExceptionIsThrown() {
        assertThatThrownBy(
                () -> CrownCourtOutcome.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("ABANDONED").isEqualTo(CrownCourtOutcome.ABANDONED.getCode());
        assertThat("TRIAL").isEqualTo(CrownCourtOutcome.ABANDONED.getType());
        assertThat("Abandoned").isEqualTo(CrownCourtOutcome.ABANDONED.getDescription());
    }
}
