package uk.gov.justice.laa.crime.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility Class for single source of handling errorLists being passed via the
 * ProblemDetail class.
 */
@UtilityClass
public class ProblemDetailErrorListUtil {

    private static final String MESSAGE_LIST_NAME = "errors";

    /**
     *
     * @param messageList List of messages to be sent with this error. ( E.g. List of all validation failure messages )
     * @param status HttpStatus for the error. E.g. HttpStatus.BAD_REQUEST for bad request.
     * @param detail String describing the error situation. ( E.g. Request failed validation. )
     * @return Problem detail made from the values supplied.
     */
    public static ProblemDetail buildProblemDetail(List<String> messageList, HttpStatus status, String detail) {
        var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setProperties(Map.of(MESSAGE_LIST_NAME, messageList));
        return problemDetail;
    }

    /**
     *
     * @param messageList List of messages to be sent with this error. ( E.g. List of all validation failure messages )
     * @param status HttpStatus for the error. E.g. HttpStatus.BAD_REQUEST for bad request.
     * @return Problem detail made from the values supplied.
     */
    public static ProblemDetail buildProblemDetail(List<String> messageList, HttpStatus status) {
        var problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setProperties(Map.of(MESSAGE_LIST_NAME, messageList));
        return problemDetail;
    }

    /**
     * @param messageList List of messages to be sent with this error. ( E.g. List of all validation failure messages )
     * @param problemDetail Problem detail that the values should be added to.
     */
    public static void addMessageList(List<String> messageList, ProblemDetail problemDetail) {
        problemDetail.setProperties(Map.of(MESSAGE_LIST_NAME, messageList));
    }

    /**
     * Helper method which takes a ProblemDetail, and returns messages from it. It will return:
     * The list of messages provided they exist.
     * If not populated, then the detail message set on the ProblemDetail itself.
     * An empty list if neither are set.
     * @param problemDetail The ProblemDetail which the message list is contained on.
     * @return List<String> of messages that were present on the ProblemDetail
     */
    public static List<String> getErrorMessages(ProblemDetail problemDetail) {
        if (Objects.isNull(problemDetail)) {
            return List.of();
        }
        // check if we're using messageList.
        List<String> messages = getMessageList(problemDetail);
        if (!messages.isEmpty()) {
            return messages;
        }
        // if empty, check incase we can get detail via another field.
        else if (Objects.nonNull(problemDetail.getDetail())) {
            return List.of(problemDetail.getDetail());
        }
        // otherwise, just return empty.
        return List.of();
    }

    @SuppressWarnings("unchecked")
    // Extract the message list if present, pass an empty list back otherwise.
    private List<String> getMessageList(ProblemDetail problemDetail) {
        Map<String, Object> properties = problemDetail.getProperties();
        if (Objects.nonNull(properties) && properties.containsKey(MESSAGE_LIST_NAME)) {
            Object messageList = properties.get(MESSAGE_LIST_NAME);
            // return the list if it's a non-empty list of strings, as expected.
            if (messageList instanceof List
                    && !((List<?>) messageList).isEmpty()
                    && ((List<?>) messageList).getFirst().getClass().equals(String.class)) {
                return (List<String>) messageList;
            }
        }
        return Collections.emptyList();
    }
}
