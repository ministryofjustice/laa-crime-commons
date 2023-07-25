package uk.gov.justice.laa.crime.commons.common;

/**
 * Constants class
 */
public class Constants {

    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * The constant LAA_TRANSACTION_ID
     * Used to trace requests spanning multiple LAA crime microservices
     */
    public static final String LAA_TRANSACTION_ID = "Laa-Transaction-Id";

    /**
     * The constant MAX_IN_MEMORY_SIZE
     * Used to limit the number of bytes that can be buffered by the WebClient whenever the input stream needs to be aggregated.
     */
    public static final int MAX_IN_MEMORY_SIZE = 10485760;

}
