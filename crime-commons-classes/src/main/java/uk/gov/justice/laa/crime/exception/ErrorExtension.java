package uk.gov.justice.laa.crime.exception;

import java.util.List;
import java.util.Objects;


public record ErrorExtension(String code, String traceId, List<ErrorMessage> errors) {

    public static final String EXTENSION_KEY = "errors";

    public boolean hasErrors() {
        return (Objects.nonNull(errors) && !errors.isEmpty());
    }
}
