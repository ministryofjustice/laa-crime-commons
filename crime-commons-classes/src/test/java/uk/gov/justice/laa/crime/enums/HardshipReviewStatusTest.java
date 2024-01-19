package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class HardshipReviewStatusTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(HardshipReviewStatus.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(HardshipReviewStatus.getFrom("COMPLETE")).isEqualTo(HardshipReviewStatus.COMPLETE);
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThatThrownBy(
                () -> HardshipReviewStatus.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("COMPLETE").isEqualTo(HardshipReviewStatus.COMPLETE.getStatus());
        assertThat("Incomplete").isEqualTo(HardshipReviewStatus.IN_PROGRESS.getDescription());
        assertThat("COMPLETE").isEqualTo(HardshipReviewStatus.COMPLETE.getValue());
    }

}