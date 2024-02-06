package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FrequencyTest {

    @Test
    void valueOfFrequencyFromString_valueNotFound_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Frequency.getFrom("THROWS_EXCEPTION"));
    }

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(Frequency.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(Frequency.getFrom("MONTHLY")).isEqualTo(Frequency.MONTHLY);
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThatThrownBy(
                () -> Frequency.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("ANNUALLY").isEqualTo(Frequency.ANNUALLY.getCode());
        assertThat("Weekly").isEqualTo(Frequency.WEEKLY.getDescription());
        assertThat(26).isEqualTo(Frequency.TWO_WEEKLY.getWeighting());
    }
}