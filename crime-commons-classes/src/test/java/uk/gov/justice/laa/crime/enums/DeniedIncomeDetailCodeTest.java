package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DeniedIncomeDetailCodeTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(DeniedIncomeDetailCode.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(DeniedIncomeDetailCode.getFrom("MEDICAL GROUNDS")).isEqualTo(DeniedIncomeDetailCode.MEDICAL_GROUNDS);
    }

    @Test
    void givenInvalidResultString_whenGetFromIsInvoked_thenExceptionIsThrown() {
        assertThatThrownBy(
                () -> DeniedIncomeDetailCode.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("MEDICAL GROUNDS").isEqualTo(DeniedIncomeDetailCode.MEDICAL_GROUNDS.getCode());
        assertThat("Medical Grounds").isEqualTo(DeniedIncomeDetailCode.MEDICAL_GROUNDS.getDescription());
        assertThat("INCOME").isEqualTo(DeniedIncomeDetailCode.MEDICAL_GROUNDS.getType());
    }

}
