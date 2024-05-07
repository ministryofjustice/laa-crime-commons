package uk.gov.justice.laa.crime.orchestration.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

public enum AssessmentResult {
    PASS, FAIL, INEL, FULL;

    public static AssessmentResult getFrom(String result) {
        if (StringUtils.isBlank(result)) return null;

        return Stream.of(AssessmentResult.values())
                .filter(assessmentResult -> assessmentResult.toString().equalsIgnoreCase(result))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Assessment Result with value: %s does not exist.", result)));
    }
}
