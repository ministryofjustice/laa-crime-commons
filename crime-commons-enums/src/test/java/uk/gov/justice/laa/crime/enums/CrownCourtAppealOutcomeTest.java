package uk.gov.justice.laa.crime.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.justice.laa.crime.enums.CrownCourtAppealOutcome.*;

class CrownCourtAppealOutcomeTest {

    @Test
    void givenOutcomeIsEmpty_ExceptionThrown() {

        Assertions.assertThrows(ValidationException.class, () -> {
            isAppeal(null);
        });
    }

    @Test
    void givenOutComeIsSuccessful_ReturnsTrue() {

        assertAll("AppealOutcome",
                () -> assertTrue(isAppeal(SUCCESSFUL.getValue())));
    }

    @Test
    void givenOutComeIsPartSuccess_ReturnsTrue() {

        assertAll("AppealOutcome",
                () -> assertTrue(isAppeal(PART_SUCCESS.getValue())));
    }

    @Test
    void givenOutComeIsUnSuccessful_ReturnsTrue() {

        assertAll("AppealOutcome",
                () -> assertTrue(isAppeal(UNSUCCESSFUL.getValue())));
    }

    @Test
    void givenOutComeIsNotForAppeal_ReturnsFalse() {

        assertAll("AppealOutcome",
                () -> assertFalse(isAppeal("ACQUITTED")));
    }
}
