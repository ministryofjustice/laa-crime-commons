package uk.gov.justice.laa.crime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PassportAssessmentResult {
    PASS("PASS"),
    FAIL("FAIL"),
    TEMP("TEMP"),
    FAIL_CONTINUE("FAIL CONTINUE");

    private String result;

    public static PassportAssessmentResult getFrom(String result) {
        if (StringUtils.isBlank(result)) return null;

        return Stream.of(PassportAssessmentResult.values())
                .filter(p -> p.result.equals(result))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Passport Assessment Result with value: %s does not exist.", result)));
    }
}
