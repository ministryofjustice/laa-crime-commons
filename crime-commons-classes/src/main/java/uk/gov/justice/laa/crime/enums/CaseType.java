package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;
import java.util.Set;

/**
 * static data migrated from TOGDATA.CASE_TYPES table
 */
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum CaseType {
    INDICTABLE("INDICTABLE", "Indictable", Boolean.TRUE),
    SUMMARY_ONLY("SUMMARY ONLY", "Summary-only", Boolean.TRUE),
    CC_ALREADY("CC ALREADY","Trial already in Crown Court", Boolean.TRUE),
    APPEAL_CC("APPEAL CC","Appeal to Crown Court", Boolean.FALSE),
    COMMITAL("COMMITAL","Committal for Sentence", Boolean.TRUE),
    EITHER_WAY("EITHER WAY","Either-Way", Boolean.FALSE);

    @NotNull
    @JsonPropertyDescription("Specifies the case type")
    private final String caseTypeString;
    private final String description;
    private final Boolean mcooOutcomeRequired;

    @JsonValue
    public String getCaseType() {
        return caseTypeString;
    }

    public static CaseType getFrom(String caseType) {
        if (StringUtils.isBlank(caseType)) return null;

        return Stream.of(CaseType.values())
                .filter(f -> f.caseTypeString.equals(caseType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("CaseType with value: %s does not exist.", caseType)));
    }

    public static boolean isMagsCaseType(String caseType) {

        if (StringUtils.isBlank(caseType)) return false;

        return Set.of(CaseType.INDICTABLE.getCaseType(), CaseType.SUMMARY_ONLY.getCaseType(), CaseType.EITHER_WAY.getCaseType())
                .contains(caseType);
    }
}