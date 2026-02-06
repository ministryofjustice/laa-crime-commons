package uk.gov.justice.laa.crime.util;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import uk.gov.justice.laa.crime.error.ErrorExtension;
import uk.gov.justice.laa.crime.error.ErrorMessage;

/**
 * Utility Class for single source for handling errors being passed via the ProblemDetail class.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProblemDetailUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().addMixIn(ProblemDetail.class, ProblemDetailJacksonMixin.class);
    public static final String FALLBACK_DETAIL_FIELD_NAME = "request";

    /**
     * Creates a ProblemDetail object with minimal standard fields.
     *
     * @param status    HttpStatusCode Http Error encountered, as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-status">RFC-9457 -
     *                  status</a>
     * @param detail    String Details the error encountered, as per <a
     *                  href="https://www.rfc-editor.org/rfc/rfc9457#name-detail">RFC-9457 -
     *                  detail</a>
     * @param extension ErrorExtension holding additional details of the error.
     * @return ProblemDetail populated with given details
     */
    public static ProblemDetail buildProblemDetail(HttpStatusCode status, String detail,
            ErrorExtension extension) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setProperty(ErrorExtension.EXTENSION_KEY, extension);
        return problemDetail;
    }

    /**
     * Creates a ProblemDetail object for the given status and parameters.
     *
     * @param status    HttpStatusCode as per <a
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
    public static ProblemDetail buildProblemDetail(HttpStatusCode status, String title, URI type,
            String detail, URI instance, ErrorExtension extension) {
        ProblemDetail problemDetail = buildProblemDetail(status, detail, extension);
        problemDetail.setInstance(instance);
        problemDetail.setTitle(title);
        problemDetail.setType(type);
        return problemDetail;
    }

    /**
     * Returns a ProblemDetail object for the given status and parameters.
     *
     * @param status    HttpStatusCode as per <a
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
    public static ProblemDetail buildProblemDetail(HttpStatusCode status, String title, String type,
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
     * @return {@code List<String>}
     * <ul>
     * <li>The list of messages provided they exist.</li>
     * <li>A list containing the detail field of the {@code ProblemDetail}</li>
     * <li>An empty list if neither are set.</li></ul>
     */
    public static List<String> getErrorDetails(ProblemDetail problemDetail) {
        return getErrorMessages(problemDetail).stream().map(ErrorMessage::message).toList();
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
                .map(ErrorExtension::errors)
                .orElseGet(() ->
                        Optional.ofNullable(problemDetail)
                                .map(ProblemDetail::getDetail)
                                .filter(detail -> !detail.isBlank())
                                .map(detail -> new ErrorMessage(FALLBACK_DETAIL_FIELD_NAME, detail))
                                .map(List::of)
                                .orElseGet(Collections::emptyList));
    }


    /**
     * Utility method to return a populated ProblemDetail object from a given json String.
     * Will populate the general fields and the errorExtension ( if present in the json ).
     * @param jsonString Json String that represents the ProblemDetail.
     * @return The Populated ProblemDetail object.
     * @throws JsonProcessingException If json is malformed, or non-compliant to ProblemDetails.
     */
    public static ProblemDetail parseProblemDetailJson(String jsonString) throws JsonProcessingException {
        ProblemDetail problemDetail = OBJECT_MAPPER.readValue(jsonString, ProblemDetail.class);
        Optional<ErrorExtension> errorExtension = getErrorExtension(problemDetail);
        errorExtension.ifPresent(ex -> problemDetail.setProperty(ErrorExtension.EXTENSION_KEY, errorExtension.get()));
        return problemDetail;
    }

    /**
     * Extracts the ErrorExtension from a given ProblemDetail if it exists.
     *
     * @param problemDetail ProblemDetail to extract ErrorExtension from.
     * @return Optional of ErrorExtension
     */
    public static Optional<ErrorExtension> getErrorExtension(ProblemDetail problemDetail) {
        return Optional.ofNullable(problemDetail)
                .filter(x -> Objects.nonNull(problemDetail.getProperties()))
                .filter(x -> x.getProperties().containsKey(ErrorExtension.EXTENSION_KEY))
                .map(x ->
                        parseExtension(problemDetail.getProperties().get(ErrorExtension.EXTENSION_KEY)));
    }

    /**
     * Utility method to convert an ErrorExtension that might be incorrectly set to LinkedHashMap into
     * the proper ErrorExtension type.
     * This will generally be when deserialized when received from an API call due to the generic nature of the
     * Properties Map.
     * @param errorExtension Object that should be cast to the ErrorExtension type.
     * @return ErrorExtension, or null if not present, or non-LinkedHashMap type.
     */
    private static ErrorExtension parseExtension(Object errorExtension) {
        ErrorExtension extension;
        // check the type of the extension. If generic convert it to ErrorExtension.
        switch (errorExtension) {
            case ErrorExtension ex -> extension = ex;
            case Map<?, ?> map -> extension = OBJECT_MAPPER.convertValue(map, ErrorExtension.class);
            case null, default -> extension = null;
        }
        return extension;
    }
}
