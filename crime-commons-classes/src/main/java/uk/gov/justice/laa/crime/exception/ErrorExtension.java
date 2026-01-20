package uk.gov.justice.laa.crime.exception;

import java.util.List;
import java.util.Objects;

/**
 * Extension payload containing additional error details for a failed request.
 *
 * @param code    an application-specific error code for identifying the failure
 * @param traceId the trace ID for correlating this error across logs/traces
 * @param errors  the list of {@link ErrorMessage} instances describing the errors encountered
 */
public record ErrorExtension(String code, String traceId, List<ErrorMessage> errors) {

    public static final String EXTENSION_KEY = "errors";

    public boolean hasErrors() {
        return (Objects.nonNull(errors) && !errors.isEmpty());
    }
}
