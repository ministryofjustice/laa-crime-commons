package uk.gov.justice.laa.crime.util;

import lombok.experimental.UtilityClass;
import uk.gov.justice.laa.crime.exception.FileReadException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class FileUtils {

    private static final String ERROR_MESSAGE_FORMAT = "Unable to read file with filePath [%s]";

    /**
     * Read the entire contents of a file and return it as a String.
     *
     * @param filePath e.g. "data/reporder_dto.json"
     * @return String representation of the file contents
     */
    public String readFileToString(String filePath) {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        URL path = classLoader.getResource(filePath);
        if (path == null) {
            throw new FileReadException(ERROR_MESSAGE_FORMAT.formatted(filePath));
        }

        File file = new File(path.getFile());
        try {
            return org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileReadException(ERROR_MESSAGE_FORMAT.formatted(filePath), e);
        }
    }
}
