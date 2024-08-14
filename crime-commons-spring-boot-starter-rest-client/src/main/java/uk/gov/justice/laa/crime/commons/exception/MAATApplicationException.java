package uk.gov.justice.laa.crime.commons.exception;

/**
 * <class>ValidationException</class>
 */
public class MAATApplicationException extends RuntimeException {

    /**
     * Constructs an instance of <code>ValidationException</code>.
     */
    public MAATApplicationException() {
        super();
    }

    /**
     * Constructs an instance of <code>ValidationException</code> with
     * the specified detail message.
     * @param message The detail message.
     */
    public MAATApplicationException(String message) {
        super(message);
    }

}
