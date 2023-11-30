package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class RepOrderStatusTest {

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(RepOrderStatus.getFrom("CURR")).isEqualTo(RepOrderStatus.CURR);
    }

    @Test
    void givenBlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(RepOrderStatus.getFrom(null)).isNull();
    }

    @Test
    void givenInvalidResultString_whenGetFromIsInvoked_thenExceptionIsThrown() {
        assertThatThrownBy(
                () -> RepOrderStatus.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("ERR").isEqualTo(RepOrderStatus.ERR.getCode());
        assertThat("Created in Error").isEqualTo(RepOrderStatus.ERR.getDescription());
        assertThat(3).isEqualTo(RepOrderStatus.ERR.getSequence());
        assertThat(RepOrderStatus.ERR.isRemoveContribs()).isTrue();
        assertThat(RepOrderStatus.ERR.isUpdateAllowed()).isFalse();
    }
}
