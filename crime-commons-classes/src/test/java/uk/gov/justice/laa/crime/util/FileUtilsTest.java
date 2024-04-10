package uk.gov.justice.laa.crime.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileUtilsTest {

    @Test
    void verifyThatFileIsReadToStringWhenSpecifiedFileExists() {
        assertNotNull(FileUtils.readFileToString("data/reporder_dto.json"));
    }

    @Test
    void verifyThatRuntimeExceptionIsThrownWhenSpecifiedFileDoesNotExist() {
        assertThrows(RuntimeException.class, () -> FileUtils.readFileToString("data/nofileexists.json"));
    }

}
