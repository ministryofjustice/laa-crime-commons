package uk.gov.justice.laa.crime.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import uk.gov.justice.laa.crime.exception.ErrorExtension;
import uk.gov.justice.laa.crime.exception.ErrorMessage;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Utility Class for single source of handling errors being passed via the ProblemDetail class.
 */
@UtilityClass
public class ProblemDetailUtil {

    /***
     * Creates a basic ProblemDetail object with minimal standard objects.
     * @param detail String Details the error encountered, as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-detail">RFC-9457</a>
     * @param status HttpStatus Http Error encountered, as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-status">RFC-9457</a>
     * @param extension ErrorExtension holding additional details of the error.
     * @return ProblemDetaill populated with given details
     */
    public static ProblemDetail buildProblemDetail(String detail, HttpStatus status, ErrorExtension extension) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setProperties(Map.of(ErrorExtension.ERROR_PARAMETER_NAME, extension));
        return problemDetail;
    }

    /***
     * Creates a ProblemDetail object with standard objects provided in Types needed.
     * @param title String as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-title">RFC-9457</a>
     * @param type URI as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-type">RFC-9457</a>
     * @param status String as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-status">RFC-9457</a>
     * @param detail String as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-detail">RFC-9457</a>
     * @param instance URI as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-instance">RFC-9457</a>
     * @param extension ErrorExtension holding additional details of the error.
     * @return ProblemDetail populated with given values.
     */
    public static ProblemDetail buildProblemDetail(String title, URI type, HttpStatus status, String detail, URI instance, ErrorExtension extension) {
        ProblemDetail problemDetail = buildProblemDetail(detail, status, extension);
        problemDetail.setInstance(instance);
        problemDetail.setTitle(title);
        problemDetail.setType(type);
        return problemDetail;
    }

    /***
     * Creates a ProblemDetail object with all standard objects provided as Strings.
     * @param title String as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-title">RFC-9457</a>
     * @param type URI String as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-type">RFC-9457</a>
     * @param status String as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-status">RFC-9457</a>
     * @param detail String as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-detail">RFC-9457</a>
     * @param instance URI String as per <a href="https://www.rfc-editor.org/rfc/rfc9457#name-instance">RFC-9457</a>
     * @param extension ErrorExtension holding additional details of the error.
     * @return ProblemDetail populated with given values.
     */
    public static ProblemDetail buildProblemDetail(String title, String type, HttpStatus status, String detail, String instance, ErrorExtension extension) {
        return buildProblemDetail(title, URI.create(type), status, detail, URI.create(instance), extension);
    }

    /***
     * Builds an ErrorExtension from given details to be used with a ProblemDetail
     * @param code String
     * @param traceId String
     * @param errors List&lt;ErrorMessage&gt; List of errors encountered
     * @return ErrorExtension
     */
    public static ErrorExtension buildErrorExtension( String code, String traceId, List<ErrorMessage> errors ) {
        return new ErrorExtension( code, traceId, errors);
    }

    /***
     * Method to get an optional of ErrorExtension for use in other methods.
     * @param problemDetail ProblemDetail of interest.
     * @return Optional of the ErrorExtension in provided ProblemDetail.
     */
    private Optional<ErrorExtension> getOptionalErrorExtension(ProblemDetail problemDetail){
        return Optional.ofNullable(problemDetail)
                .map(ProblemDetail::getProperties)
                .map(properties -> properties.get(ErrorExtension.ERROR_PARAMETER_NAME))
                .filter(ErrorExtension.class::isInstance)
                .map(ErrorExtension.class::cast);
    }

    /**
     * Returns the error messages present on the given ProblemDetail.
     * Returns
     * list of errors if they are present and populated.
     * The detail of problemDetail if no list is present
     * empty list if neither are present.
     * @param problemDetail ProblemDetail the object containing the errors.
     * @return List of ErrorMessages, or an empty list if none are present as per above.
     */
    public ErrorExtension getErrorExtension(ProblemDetail problemDetail){
        return getOptionalErrorExtension(problemDetail).orElse(null);
    }

    /**
     * Helper method which takes a ProblemDetail, and returns the messages from it as a List of Strings.
     * It will return:
     * The list of messages provided they exist.
     * If not populated, then the detail message set on the ProblemDetail itself.
     * An empty list if neither are set.
     * @param problemDetail The ProblemDetail which the message list is contained on.
     * @return List&lt;String&gt; of messages that were present on the ProblemDetail
     */
    public List<String> getErrorDetails(ProblemDetail problemDetail){
        return getErrorMessages(problemDetail).stream().map(ErrorMessage::getMessage).toList();
    }

    /**
     * Helper method which takes a ProblemDetail, and returns the ErrorMessages from it.
     * It will return:
     * The list of messages provided they exist.
     * If not populated, then the detail message set on the ProblemDetail itself.
     * An empty list if neither are set.
     * @param problemDetail The ProblemDetail which the message list is contained on.
     * @return List&lt;ErrorMessage&gt; of messages that were present on the ProblemDetail
     */
    public List<ErrorMessage> getErrorMessages(ProblemDetail problemDetail) {
        return getOptionalErrorExtension(problemDetail)
                .filter(ErrorExtension::hasMessages)
                .map(ErrorExtension::getErrors)
                .orElseGet(() ->
                        Optional.ofNullable(problemDetail)
                        .map(ProblemDetail::getDetail)
                        .map(detail -> new ErrorMessage("Request", detail))
                        .map(List::of)
                        .orElseGet(List::of));
    }

}
