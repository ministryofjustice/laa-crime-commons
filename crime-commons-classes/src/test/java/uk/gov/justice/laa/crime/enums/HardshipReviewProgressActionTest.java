package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class HardshipReviewProgressActionTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(HardshipReviewProgressAction.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(HardshipReviewProgressAction.getFrom("FURTHER INFO")).isEqualTo(HardshipReviewProgressAction.FURTHER_INFO);
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThatThrownBy(
                () -> HardshipReviewProgressAction.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("OTHER").isEqualTo(HardshipReviewProgressAction.OTHER.getAction());
        assertThat("Rejected application").isEqualTo(HardshipReviewProgressAction.REJECTED_APP.getDescription());
    }

}