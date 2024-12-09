package uk.gov.justice.laa.crime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AssessmentResult {
    PASS("PASS", "Gross income below the lower threshold"),
    FAIL("FAIL", "Gross income above the upper threshold"),
    HARDSHIP("HARDSHIP APPLICATION", "Hardship application"),
    INEL("INEL", "Refused - Ineligible"),
    FULL("FULL", "Gross income in between the upper and lower thresholds");

    private String result;
    private String reason;

    public static AssessmentResult getFrom(String result) {
        if (StringUtils.isBlank(result)) return null;

        return Stream.of(AssessmentResult.values())
                .filter(assessmentResult -> assessmentResult.getResult().equalsIgnoreCase(result))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Assessment Result with value: %s does not exist.", result)));
    }
}
