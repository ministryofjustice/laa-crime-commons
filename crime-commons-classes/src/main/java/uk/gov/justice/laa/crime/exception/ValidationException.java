package uk.gov.justice.laa.crime.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationException extends RuntimeException {
    private List<String> errorMessageList;

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable rootCause) {
        super(rootCause);
    }

    public ValidationException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public ValidationException(List<String> errorMessageList) {
        this.errorMessageList = errorMessageList;
    }
}
