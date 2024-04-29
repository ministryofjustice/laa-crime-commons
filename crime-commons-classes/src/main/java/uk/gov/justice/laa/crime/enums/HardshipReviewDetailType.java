package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum HardshipReviewDetailType {

    FUNDING("FUNDING", "Other Sources of Funding"),
    INCOME("INCOME", "Income Denied Access To"),
    EXPENDITURE("EXPENDITURE", "Extra Expenditure"),
    SOL_COSTS("SOL COSTS", "Solicitor Costs"),
    ACTION("ACTION", "Review Progress");

    @JsonPropertyDescription("This will have hardship review detail type")
    @JsonValue
    private final String type;
    private final String description;

    public static HardshipReviewDetailType getFrom(String type) {
        if (StringUtils.isBlank(type)) return null;

        return Stream.of(HardshipReviewDetailType.values())
                .filter(hrd -> hrd.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("hardship review detail with type: %s does not exist.", type)));
    }
}
