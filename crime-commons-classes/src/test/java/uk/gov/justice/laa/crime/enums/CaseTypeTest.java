package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CaseTypeTest {


    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(CaseType.getFrom(null)).isNull();
    }

    @Test
    void givenValidResultString_whenGetFromIsInvoked_thenCorrectEnumIsReturned() {
        assertThat(CaseType.getFrom("INDICTABLE")).isEqualTo(CaseType.INDICTABLE);
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThatThrownBy(
                () -> CaseType.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidInput_ValidateEnumValues() {
        assertThat("INDICTABLE").isEqualTo(CaseType.INDICTABLE.getCaseTypeString());
        assertThat("Indictable").isEqualTo(CaseType.INDICTABLE.getDescription());
        assertThat(Boolean.TRUE).isEqualTo(CaseType.INDICTABLE.getMcooOutcomeRequired());
    }

    @Test
    void givenAValidInput_whenIsMagsCaseTypeIsInvoked_thenTrueIsReturned() {
        assertThat(CaseType.isMagsCaseType(CaseType.INDICTABLE.getCaseType())).isTrue();
        assertThat(CaseType.isMagsCaseType(CaseType.SUMMARY_ONLY.getCaseType())).isTrue();
        assertThat(CaseType.isMagsCaseType(CaseType.EITHER_WAY.getCaseType())).isTrue();
    }

    @Test
    void givenACommitalCaseType_whenIsMagsCaseTypeIsInvoked_thenFalseIsReturned() {
        assertThat(CaseType.isMagsCaseType(CaseType.COMMITAL.getCaseType())).isFalse();
    }

}