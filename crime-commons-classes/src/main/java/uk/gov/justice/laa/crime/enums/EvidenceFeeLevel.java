package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

/**
 * static data migrated from TOGDATA.EVIDENCE_FEE_LEVELS
 */
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum EvidenceFeeLevel {
    LEVEL1("LEVEL1", "Full Fee"),
    LEVEL2("LEVEL2", "Half Fee");

    @JsonPropertyDescription("Specifies the Evidence Fee Levels")
    private final String feeLevel;
    private final String description;


    public static EvidenceFeeLevel getFrom(String level) {
        if (StringUtils.isBlank(level)) return null;

        return Stream.of(EvidenceFeeLevel.values())
                .filter(f -> f.feeLevel.equals(level))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Fee Level with value: %s does not exist.", level)));
    }
}