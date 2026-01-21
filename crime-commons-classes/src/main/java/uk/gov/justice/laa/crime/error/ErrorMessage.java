package uk.gov.justice.laa.crime.error;

/**
 * Represents a single validation or business rule error.
 *
 * @param field   the name of the field/property the error relates to (may be {@code null} for global errors)
 * @param message the human-readable error message
 */
public record ErrorMessage(String field, String message) { }
