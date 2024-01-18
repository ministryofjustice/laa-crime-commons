package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CrownCourtOutcomeTypeTest {

    @Test
    void givenValidType_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(CrownCourtOutcomeType.getFrom("TRIAL")).isEqualTo(CrownCourtOutcomeType.TRIAL);
    }

    @Test
    void givenBlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(CrownCourtOutcomeType.getFrom("")).isEqualTo(null);
    }

    @Test
    void givenInvalidType_whenGetFromIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> CrownCourtOutcomeType.getFrom("FLIBBLE")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("APPEAL").isEqualTo(CrownCourtOutcomeType.APPEAL.getType());
    }
}
