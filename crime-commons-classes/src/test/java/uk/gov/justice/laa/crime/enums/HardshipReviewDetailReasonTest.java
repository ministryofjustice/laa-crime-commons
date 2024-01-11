package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class HardshipReviewDetailReasonTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(HardshipReviewDetailReason.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(HardshipReviewDetailReason.getFrom("Evidence Supplied")).isEqualTo(HardshipReviewDetailReason.EVIDENCE_SUPPLIED);
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThatThrownBy(
                () -> HardshipReviewDetailReason.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenInvalidResultString_whenGetFromIsInvoked_thenExceptionIsThrown() {
        assertThatThrownBy(
                () -> HardshipReviewDetailReason.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("Evidence Supplied").isEqualTo(HardshipReviewDetailReason.EVIDENCE_SUPPLIED.getReason());
        assertThat(7).isEqualTo(HardshipReviewDetailReason.EVIDENCE_SUPPLIED.getId());
    }

}