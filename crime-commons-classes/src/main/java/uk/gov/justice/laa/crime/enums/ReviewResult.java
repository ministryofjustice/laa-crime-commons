package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum ReviewResult {

    PASS("PASS"),
    FAIL("FAIL");

    @NotNull
    @JsonPropertyDescription("The review result")
    private final String result;


    public static ReviewResult getFrom(String result) {
        if (StringUtils.isBlank(result)) {
            return null;
        }

        return Stream.of(ReviewResult.values())
                .filter(reviewResult -> reviewResult.result.equals(result))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Review result with value: %s does not exist.", result)));
    }
}
