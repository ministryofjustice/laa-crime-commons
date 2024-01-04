package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrentStatusTest {

    @Test
    void valueOfCurrentStatusFromString_success() {
        assertEquals(CurrentStatus.IN_PROGRESS, CurrentStatus.getFrom("IN PROGRESS"));
    }

    @Test
    void valueOfCurrentStatusFromString_nullParameter_ReturnsNull() {
        assertThrows(IllegalArgumentException.class, () -> CurrentStatus.getFrom(null));
    }

    @Test
    void valueOfCurrentStatusFromString_valueNotFound_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> CurrentStatus.getFrom("THROWS_EXCEPTION"));
    }
}