package uk.gov.justice.laa.crime.orchestration.enums.cat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum AssessmentType {

    MCHARDSHIP("MCHARDSHIP"),
    MEANS_INIT("MEANS_INIT"),
    CCHARDSHIP("CCHARDSHIP"),
    IOJ("IOJ"),
    MEANS_FULL("MEANS_FULL"),
    PASSPORT("PASSPORT");

    private final String code;


    public static AssessmentType getFrom(String assessmentType) {
        if (StringUtils.isBlank(assessmentType)) return null;

        return Stream.of(AssessmentType.values())
                .filter(f -> f.code.equals(assessmentType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("AssessmentType with value: %s does not exist.", assessmentType)));
    }
}
