package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum PassportAssessmentDecision {
    PASS("PASS", "Pass the passport assessment"),
    TEMP_PASS("TEMP_PASS", "Used if the DWP link is unavailable, or the defendant is remanded" 
        + "into court custody and does not know their National Insurance Number"),
    FAIL_BYPASS("FAIL_BYPASS", "Fails on the DWP check. If financial information " 
        + "is supplied the caseworker performs a means assessment, otherwise they reject the " 
        + "application back to the provider");

    @JsonPropertyDescription("Passport assessment decision code")
    @JsonValue
    private final String code;
    private final String description;

    public static PassportAssessmentDecision getFrom(String code) throws IllegalArgumentException {
        if (StringUtils.isBlank(code)) return null;

        return Stream.of(PassportAssessmentDecision.values())
            .filter(r -> r.code.equals(code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("PassportAssessmentDecision " 
                + "with value: %s does not exist.", code)));
    }
}
