package uk.gov.justice.laa.crime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CrownCourtOutcomeType {
    TRIAL("TRIAL"), APPEAL("APPEAL");

    private final String type;

    public static CrownCourtOutcomeType getFrom(String type) {
        if (StringUtils.isBlank(type)) return null;

        return Stream.of(CrownCourtOutcomeType.values())
                .filter(ccoType -> ccoType.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("CrownCourtOutcomeType with value: %s does not exist.", type)));
    }
}
