package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {"INDICTABLE", "SUMMARY ONLY", "EITHER WAY"})
    void givenAValidInput_whenIsMagsCaseTypeIsInvoked_thenTrueIsReturned(String caseType) {
        assertThat(CaseType.isMagsCaseType(caseType)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"COMMITAL", ""})
    void givenAInvalidInput_whenIsMagsCaseTypeIsInvoked_thenFalseIsReturned(String caseType) {
        assertThat(CaseType.isMagsCaseType(caseType)).isFalse();
    }

}