package uk.gov.justice.laa.crime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum FullAssessmentResult {
    PASS("PASS", "Disposable income below the threshold"),
    FAIL("FAIL", "Disposable income above the threshold"),
    INEL("INEL", "Refused - Ineligible");

    private String result;
    private String reason;

    public static FullAssessmentResult getFrom(String result) {
        if (StringUtils.isBlank(result)) return null;

        return Stream.of(FullAssessmentResult.values())
                .filter(a -> a.result.equals(result))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Full Assessment Result with value: %s does not exist.", result)));
    }
}
