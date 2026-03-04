package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum PassportAssessmentDecisionReason {
    DWP_CHECK("DWP", "DWP"),
    DOCUMENTATION_SUPPLIED("DOCSUP", "Documentation supplied"),
    APPLICANT_AGE("AGEREL", "Age Related"),
    DWP_CHECK_UNAVAILABLE("NOCONFPOS", "DWP Unavailable"),
    IN_CUSTODY("INCUSTODY", "In Custody");

    @JsonPropertyDescription("Passport assessment decision reason confirmation")
    @JsonValue
    private final String confirmation;
    private final String description;

    public static PassportAssessmentDecisionReason getFrom(String confirmation)
        throws IllegalArgumentException {
        if (StringUtils.isBlank(confirmation)) {
            return null;
        }

        return Stream.of(PassportAssessmentDecisionReason.values())
            .filter(r -> r.confirmation.equals(confirmation))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException(String.format("PassportAssessmentDecisionReason "
                    + "with value: %s does not exist.", confirmation)));
    }
}
