package uk.gov.justice.laa.crime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ReviewType {

    ER("ER", "Eligibility Review"),
    EM("EM", "Eligibility Miscalculation Review"),
    NAFI("NAFI", "New Application Following Ineligibility");

    private final String code;
    private final String description;

    public static ReviewType getFrom(String code) throws IllegalArgumentException {
        if (StringUtils.isBlank(code)) return null;

        return Stream.of(ReviewType.values())
                .filter(r -> r.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("ReviewType with value: %s does not exist.", code)));
    }
}
