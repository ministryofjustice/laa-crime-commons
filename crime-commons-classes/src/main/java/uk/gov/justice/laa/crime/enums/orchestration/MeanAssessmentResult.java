package uk.gov.justice.laa.crime.orchestration.enums.cat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum MeanAssessmentResult {

    FULL("FULL"),
    PASS("PASS"),
    FAIL("FAIL"),
    HARDSHIP_APPLICATION("HARDSHIP APPLICATION"),
    INEL("INEL");

    private final String code;

    public static MeanAssessmentResult getFrom(String assessmentType) {
        if (StringUtils.isBlank(assessmentType)) return null;

        return Stream.of(MeanAssessmentResult.values())
                .filter(f -> f.code.equals(assessmentType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("MeanAssessmentResult with value: %s does not exist.", assessmentType)));
    }
}
