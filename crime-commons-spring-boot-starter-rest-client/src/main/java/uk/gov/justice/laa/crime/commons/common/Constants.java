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
}
