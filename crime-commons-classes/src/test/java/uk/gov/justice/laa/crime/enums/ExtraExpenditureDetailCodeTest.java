package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ExtraExpenditureDetailCodeTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(ExtraExpenditureDetailCode.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(ExtraExpenditureDetailCode.getFrom("CAR LOAN")).isEqualTo(ExtraExpenditureDetailCode.CAR_LOAN);
    }

    @Test
    void givenInvalidResultString_whenGetFromIsInvoked_thenExceptionIsThrown() {
        assertThatThrownBy(
                () -> ExtraExpenditureDetailCode.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("CAR LOAN").isEqualTo(ExtraExpenditureDetailCode.CAR_LOAN.getCode());
        assertThat("Car Loan").isEqualTo(ExtraExpenditureDetailCode.CAR_LOAN.getDescription());
    }

}
