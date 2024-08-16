package uk.gov.justice.laa.crime.commons.exception;

/**
 * <class>MAATServerException</class>
 */
public class MAATServerException extends RuntimeException {

    /**
     * Constructs an instance of <code>MAATServerException</code>.
     */
    public MAATServerException() {
        super();
    }

    /**
     * Constructs an instance of <code>MAATServerException</code> with
     * the specified detail message.
     * @param message The detail message.
     */
    public MAATServerException(String message) {
        super(message);
    }

}
