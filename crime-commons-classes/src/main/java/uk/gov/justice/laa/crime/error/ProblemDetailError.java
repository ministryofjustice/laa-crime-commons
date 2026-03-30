package uk.gov.justice.laa.crime.error;

public enum ProblemDetailError {
    DB_ERROR("Request violates a data constraint"),
    VALIDATION_FAILURE("Validation failure"),
    OBJECT_NOT_FOUND("Resource not found"),
    APPLICATION_ERROR("Unexpected error"),
    METHOD_NOT_ALLOWED("HTTP method not allowed"),
    UNSUPPORTED_MEDIA_TYPE("Unsupported media type"),
    BAD_REQUEST("Invalid request");

    private final String defaultDetail;

    ProblemDetailError(String defaultDetail) {
        this.defaultDetail = defaultDetail;
    }

    public String code() {
        return name();
    }

    public String defaultDetail() {
        return defaultDetail;
    }
}