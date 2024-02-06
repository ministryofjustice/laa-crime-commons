package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EvidenceFeeLevelTest {

    @Test
    void givenABlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(EvidenceFeeLevel.getFrom(null)).isNull();
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThatThrownBy(
                () -> EvidenceFeeLevel.getFrom("MOCK_RESULT_STRING")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testGetFrom() {
        EvidenceFeeLevel result = EvidenceFeeLevel.getFrom("LEVEL1");
        assertThat(result).isEqualTo(EvidenceFeeLevel.LEVEL1);
    }

    @Test
    void testValues() {
        EvidenceFeeLevel[] result = EvidenceFeeLevel.values();
        assertThat(result).isEqualTo(new EvidenceFeeLevel[]{EvidenceFeeLevel.LEVEL1, EvidenceFeeLevel.LEVEL2});
    }

    @Test
    void testValueOf() {
        EvidenceFeeLevel result = EvidenceFeeLevel.valueOf("LEVEL1");
        assertThat(result).isEqualTo(EvidenceFeeLevel.LEVEL1);
    }
}

