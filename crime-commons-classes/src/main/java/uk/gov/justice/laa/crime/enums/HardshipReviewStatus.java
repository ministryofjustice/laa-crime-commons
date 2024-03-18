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
public enum HardshipReviewStatus {

    IN_PROGRESS("IN PROGRESS", "Incomplete"),
    COMPLETE("COMPLETE", "Complete");

    @NotNull
    @JsonPropertyDescription("This will have the status of the hardship review")
    @JsonValue
    private String status;
    private String description;

    @JsonValue
    public String getValue() {
        return this.status;
    }

    public static HardshipReviewStatus getFrom(String status) {
        if (StringUtils.isBlank(status)) return null;

        return Stream.of(HardshipReviewStatus.values())
                .filter(hrs -> hrs.status.equals(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("HardshipReviewStatus with value: %s does not exist.", status)));
    }
}
