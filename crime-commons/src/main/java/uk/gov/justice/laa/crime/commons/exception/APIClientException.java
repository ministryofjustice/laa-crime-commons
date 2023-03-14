package uk.gov.justice.laa.crime.commons.exception;

public class APIClientException extends RuntimeException {

    public APIClientException() {
        super();
    }

    public APIClientException(String message) {
        super(message);
    }

    public APIClientException(Throwable rootCause) {
        super(rootCause);
    }

    public APIClientException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
