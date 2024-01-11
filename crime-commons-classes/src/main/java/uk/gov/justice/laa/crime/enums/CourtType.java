package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CourtType {

    MAGISTRATE("MAGISTRATE", "Magistrate"),
    CROWN_COURT("CROWN COURT", "Crown Court");

    @NotNull
    @JsonPropertyDescription("This will have the court type of the hardship review")
    private String type;
    private String description;

    @JsonValue
    public String getValue() {
        return this.type;
    }

    public static CourtType getFrom(String status) {
        if (StringUtils.isBlank(status)) return null;

        return Stream.of(CourtType.values())
                .filter(hrs -> hrs.type.equals(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("CourtType with value: %s does not exist.", status)));
    }
}
