package uk.gov.justice.laa.crime.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<String> errors = new ArrayList<>();

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

    public ValidationException(List<String> errorList) {
        this.errors.addAll(errorList);
    }

    public List<String> getErrors() {
        return List.copyOf(this.errors);
    }

}
