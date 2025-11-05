package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum BenefitType {
    INCOME_SUPPORT("INCOME_SUPPORT", "Income Support"),
    JSA("JSA", "Jobseeker's Allowance"),
    ESA("ESA", "Employment and Support Allowance"),
    GSPC("GSPC", "Guaranteed State Pension Credit"),
    UC("UC", "Universal Credit");

    @JsonPropertyDescription("Benefit type")
    @JsonValue
    private final String code;
    private final String description;

    public static BenefitType getFrom(String code) throws IllegalArgumentException {
        if (StringUtils.isBlank(code)) return null;

        return Stream.of(BenefitType.values())
            .filter(r -> r.code.equals(code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("BenefitType with " 
                + "value: %s does not exist.", code)));
    }
}
