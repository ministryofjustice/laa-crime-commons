package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class HardshipReviewProgressResponseTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(HardshipReviewProgressResponse.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(HardshipReviewProgressResponse.getFrom("FURTHER RECEIVED")).isEqualTo(HardshipReviewProgressResponse.FURTHER_RECEIVED);
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThatThrownBy(
                () -> HardshipReviewProgressResponse.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("ORIGINAL RECEIVED").isEqualTo(HardshipReviewProgressResponse.ORIGINAL_RECEIVED.getResponse());
        assertThat("Additional evidence provided").isEqualTo(HardshipReviewProgressResponse.ADDITIONAL_PROVIDED.getDescription());
    }

}