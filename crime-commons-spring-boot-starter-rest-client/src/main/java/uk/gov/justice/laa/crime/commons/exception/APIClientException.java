package uk.gov.justice.laa.crime.commons.exception;

/**
 * The type Api client exception.
 * Generic top-level runtime exception used to wrap all api error responses
 */
public class APIClientException extends RuntimeException {

    /**
     * Instantiates a new Api client exception.
     */
    public APIClientException() {
        super();
    }

    /**
     * Instantiates a new Api client exception.
     *
     * @param message the message
     */
    public APIClientException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Api client exception.
     *
     * @param rootCause the root cause
     */
    public APIClientException(Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Instantiates a new Api client exception.
     *
     * @param message   the message
     * @param rootCause the root cause
     */
    public APIClientException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
