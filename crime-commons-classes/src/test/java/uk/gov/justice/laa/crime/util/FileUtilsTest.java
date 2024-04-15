package uk.gov.justice.laa.crime.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.exception.FileReadException;

import java.io.File;
import java.io.UTFDataFormatException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FileUtilsTest {

    @Test
    void verifyThatFileIsReadToStringWhenSpecifiedFileExists() {
        assertNotNull(FileUtils.readFileToString("data/reporder_dto.json"));
    }

    @Test
    void verifyThatRuntimeExceptionIsThrownWhenSpecifiedFileDoesNotExist() {
        assertThrows(FileReadException.class, () -> FileUtils.readFileToString("data/nofileexists.json"));
    }

    @Test
    void verifyThatRunTimeExceptionIsThrownOnValidIOException() {
        try(MockedStatic<org.apache.commons.io.FileUtils> fileUtils = Mockito.mockStatic(org.apache.commons.io.FileUtils.class)) {
            fileUtils.when(() -> org.apache.commons.io.FileUtils.readFileToString(any(File.class), any(Charset.class))).thenThrow(UTFDataFormatException.class);
            assertThrows(FileReadException.class, () -> FileUtils.readFileToString("data/reporder_dto.json"));
        }
    }

}
