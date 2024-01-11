package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class HardshipReviewDetailCodeTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(HardshipReviewDetailCode.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(HardshipReviewDetailCode.getFrom("BAILIFF")).isEqualTo(HardshipReviewDetailCode.BAILIFF);
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThatThrownBy(
                () -> HardshipReviewDetailCode.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("ADD MORTGAGE").isEqualTo(HardshipReviewDetailCode.ADD_MORTGAGE.getCode());
        assertThat("Car Loan").isEqualTo(HardshipReviewDetailCode.CAR_LOAN.getDescription());
    }

}
