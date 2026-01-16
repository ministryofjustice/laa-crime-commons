package uk.gov.justice.laa.crime.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * Container for relevant information about the problem encountered.
 * code: String for additional identification of the error.
 * traceId: The trace id of the transaction.
 * errors: {@literal List<ErrorMessage>} of problems encountered during transaction.
 */
@AllArgsConstructor
@Data
public class ErrorExtension {
    private final String code;
    private final String traceId;
    private final List<ErrorMessage> errors;

    public static final String EXTENSION_KEY = "errors";

    public boolean hasErrors(){
        return (Objects.nonNull(errors) && !errors.isEmpty());
    }

}
