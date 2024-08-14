package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum EmploymentStatus {
    EMPLOY("EMPLOY", "Employed"),
    SELF("SELF", "Self Employed"),
    SFLF("SFLF", "Crown Court Case"),
    NONPASS("NONPASS", "Unemployed"),
    EMPCDS("EMPCDS", "Employed with SoT"),
    SELF_SOT("SELF-SOT", "Self Employed with SoT"),
    PASSPORTED("PASSPORTED", "Passported Benefits"),
    SELF_CASH("SELF-CASH", "Self Employed - Cash in Hand"),
    EMPLOYED_CASH("EMPLOYED-CASH", "Employed Cash in Hand"),
    YOUTHS_YTH("YOUTHS-YTH", "Youths - Under 18 heard in the Youth court"),
    YOUTHS_MAGS("YOUTHS-MAGS", "Youths - Under 18 heard in the Mags court");

    private final String code;
    private final String description;

    public static EmploymentStatus getFrom(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        return Stream.of(EmploymentStatus.values())
                .filter(status -> status.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Employment status with value: %s does not exist.", code)));
    }

}
