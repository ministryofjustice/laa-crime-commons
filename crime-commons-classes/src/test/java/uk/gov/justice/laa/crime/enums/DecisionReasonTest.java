package uk.gov.justice.laa.crime.crowncourt.staticdata.enums;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.enums.MagCourtOutcome;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DecisionReasonTest {


    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(DecisionReason.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(DecisionReason.getFrom("ABANDONED")).isEqualTo(DecisionReason.ABANDONED);
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThatThrownBy(
                () -> MagCourtOutcome.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("ABANDONED").isEqualTo(DecisionReason.ABANDONED.getCode());
        assertThat("Abandoned").isEqualTo(DecisionReason.ABANDONED.getDescription());
    }

}