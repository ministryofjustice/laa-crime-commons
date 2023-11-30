package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrequencyTest {

    @Test
    void valueOfFrequencyFromString_success() {
        assertEquals(Frequency.TWO_WEEKLY, Frequency.getFrom("2WEEKLY"));
    }

    @Test
    void valueOfFrequencyFromString_nullParamenter_ReturnsNull() {
        assertNull(Frequency.getFrom(null));
    }

    @Test
    void valueOfFrequencyFromString_valueNotFound_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Frequency.getFrom("THROWS_EXCEPTION"));
    }
}