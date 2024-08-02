package uk.gov.justice.laa.crime.commons.exception;

/**
 * <class>ValidationException</class>
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs an instance of <code>ValidationException</code>.
     */
    public ValidationException() {
        super();
    }

    /**
     * Constructs an instance of <code>ValidationException</code> with
     * the specified detail message.
     * @param message The detail message.
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs an instance of <code>ValidationException</code> with
     * the specified root cause.
     * @param rootCause The root cause of this exception
     */
    public ValidationException(Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Constructs an instance of <code>ValidationException</code> with
     * the specified root cause and detail message.
     * @param message The detail message.
     * @param rootCause The root cause of this exception
     */
    public ValidationException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}
