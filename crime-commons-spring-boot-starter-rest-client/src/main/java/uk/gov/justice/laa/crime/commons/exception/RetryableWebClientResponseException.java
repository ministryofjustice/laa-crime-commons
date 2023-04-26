package uk.gov.justice.laa.crime.commons.exception;

/**
 * The type Retryable web client response exception.
 * Runtime exception that indicates a potentially retryable error response
 */
public class RetryableWebClientResponseException extends RuntimeException {

    /**
     * Instantiates a new Retryable web client response exception.
     */
    public RetryableWebClientResponseException() {
        super();
    }

    /**
     * Instantiates a new Retryable web client response exception.
     *
     * @param message the message
     */
    public RetryableWebClientResponseException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Retryable web client response exception.
     *
     * @param rootCause the root cause
     */
    public RetryableWebClientResponseException(Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Instantiates a new Retryable web client response exception.
     *
     * @param message   the message
     * @param rootCause the root cause
     */
    public RetryableWebClientResponseException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}