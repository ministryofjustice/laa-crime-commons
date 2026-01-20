package uk.gov.justice.laa.crime.util;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import uk.gov.justice.laa.crime.exception.ErrorExtension;
import uk.gov.justice.laa.crime.exception.ErrorMessage;

/**
 * Utility Class for single source for handling errors being passed via the ProblemDetail class.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProblemDetailUtil {

    public static final String FALLBACK_DETAIL_FIELD_NAME = "request";

    /**
     * Creates a ProblemDetail object with minimal standard objects.
     *
     * @param status    HttpStatus Http Error encountered, as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-status">RFC-9457 -
     *                  status</a>
     * @param detail    String Details the error encountered, as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-detail">RFC-9457 -
     *                  detail</a>
     * @param extension ErrorExtension holding additional details of the error.
     * @return ProblemDetail populated with given details
     */
    public static ProblemDetail buildProblemDetail(HttpStatus status, String detail,
            ErrorExtension extension) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setProperty(ErrorExtension.EXTENSION_KEY, extension);
        return problemDetail;
    }

    /**
     * Creates a ProblemDetail object for the given status and parameters.
     *
     * @param status    String as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-status">RFC-9457 -
     *                  status</a>
     * @param title     String as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-title">RFC-9457 -
     *                  title</a>
     * @param type      URI as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-type">RFC-9457 - type</a>
     * @param detail    String as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-detail">RFC-9457 -
     *                  detail</a>
     * @param instance  URI as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-instance">RFC-9457 -
     *                  instance</a>
     * @param extension ErrorExtension holding additional details of the error.
     * @return ProblemDetail populated with given values.
     */
    public static ProblemDetail buildProblemDetail(HttpStatus status, String title, URI type,
            String detail, URI instance, ErrorExtension extension) {
        ProblemDetail problemDetail = buildProblemDetail(status, detail, extension);
        problemDetail.setInstance(instance);
        problemDetail.setTitle(title);
        problemDetail.setType(type);
        return problemDetail;
    }

    /**
     * Returns a ProblemDetail object for the given status and String parameters.
     *
     * @param status    String as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-status">RFC-9457 -
     *                  status</a>
     * @param title     String as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-title">RFC-9457 -
     *                  title</a>
     * @param type      URI String as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-type">RFC-9457 - type</a>
     * @param detail    String as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-detail">RFC-9457 -
     *                  detail</a>
     * @param instance  URI String as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-instance">RFC-9457 -
     *                  instance</a>
     * @param extension ErrorExtension for holding additional details of the error.
     */
    public static ProblemDetail buildProblemDetail(HttpStatus status, String title, String type,
            String detail, String instance, ErrorExtension extension) {
        return buildProblemDetail(status, title, URI.create(type), detail, URI.create(instance),
                extension);
    }

    /**
     * Builds an ErrorExtension from given details to be used with a ProblemDetail
     *
     * @param code    String for additional identification of the error.
     * @param traceId TraceId of the transaction.
     * @param errors  {@literal List<ErrorMessage>} of problems encountered during transaction.
     */
    public static ErrorExtension buildErrorExtension(String code, String traceId,
            List<ErrorMessage> errors) {
        return new ErrorExtension(code, traceId, errors);
    }

    /**
     * Extracts the ErrorExtension from a given ProblemDetail if it exists.
     *
     * @param problemDetail ProblemDetail to extract ErrorExtension from.
     * @return Optional of ErrorExtension
     */
    public static Optional<ErrorExtension> getErrorExtension(ProblemDetail problemDetail) {
        return Optional.ofNullable(problemDetail)
                .map(ProblemDetail::getProperties)
                .map(properties -> properties.get(ErrorExtension.EXTENSION_KEY))
                .filter(ErrorExtension.class::isInstance)
                .map(ErrorExtension.class::cast);
    }


    /**
     * @return {@code List<String>}
     * <ul>
     * <li>The list of messages provided they exist.</li>
     * <li>A list containing the detail field of the {@code ProblemDetail}</li>
     * <li>An empty list if neither are set.</li></ul>
     */
    public static List<String> getErrorDetails(ProblemDetail problemDetail) {
        return getErrorMessages(problemDetail).stream().map(ErrorMessage::getMessage).toList();
    }

    /**
     * @return {@code List<ErrorMessages>}
     * <ul>
     * <li>The list of messages provided they exist.</li>
     * <li>A list containing the detail field of the {@code ProblemDetail} as an {@code ErrorMessage}.</li>
     * <li>An empty list if neither are set.</li></ul>
     */
    public static List<ErrorMessage> getErrorMessages(ProblemDetail problemDetail) {
        return getErrorExtension(problemDetail)
                .filter(ErrorExtension::hasErrors)
                .map(ErrorExtension::getErrors)
                .orElseGet(() ->
                        Optional.ofNullable(problemDetail)
                                .map(ProblemDetail::getDetail)
                                .filter(detail -> !detail.isBlank())
                                .map(detail -> new ErrorMessage(FALLBACK_DETAIL_FIELD_NAME, detail))
                                .map(List::of)
                                .orElseGet(Collections::emptyList));
    }

}
