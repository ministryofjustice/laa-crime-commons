package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CourtTypeTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(CourtType.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(CourtType.getFrom("MAGISTRATE")).isEqualTo(CourtType.MAGISTRATE);
    }

    @Test
    void givenInvalidResultString_whenGetFromIsInvoked_thenExceptionIsThrown() {
        assertThatThrownBy(
                () -> CourtType.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("MAGISTRATE").isEqualTo(CourtType.MAGISTRATE.getValue());
        assertThat("MAGISTRATE").isEqualTo(CourtType.MAGISTRATE.getType());
        assertThat("Crown Court").isEqualTo(CourtType.CROWN_COURT.getDescription());
    }

}
