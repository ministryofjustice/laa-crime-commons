package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum HardshipReviewProgressResponse {

    FURTHER_RECEIVED("FURTHER RECEIVED", "Further Information received"),
    ORIGINAL_RECEIVED("ORIGINAL RECEIVED", "Original application received from HMCS"),
    ADDITIONAL_PROVIDED("ADDITIONAL PROVIDED", "Additional evidence provided");

    @JsonPropertyDescription("This will have the hardship review progress response")
    @JsonValue
    private String response;
    private String description;

    public static HardshipReviewProgressResponse getFrom(String response) {
        if (StringUtils.isBlank(response)) return null;

        return Stream.of(HardshipReviewProgressResponse.values())
                .filter(hrpr -> hrpr.response.equals(response))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("hardship review progress response with type: %s does not exist.", response)));
    }
}
