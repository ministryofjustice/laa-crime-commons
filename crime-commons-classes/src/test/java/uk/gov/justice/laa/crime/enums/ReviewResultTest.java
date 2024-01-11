package uk.gov.justice.laa.crime.crowncourt.staticdata.enums;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.enums.CaseType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ReviewResultTest {


    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(ReviewResult.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(ReviewResult.getFrom("PASS")).isEqualTo(ReviewResult.PASS);
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThatThrownBy(
                () -> CaseType.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("PASS").isEqualTo(ReviewResult.PASS.getResult());

    }

}