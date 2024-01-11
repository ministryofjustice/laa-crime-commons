package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DeniedIncomeDetailCode {

    MEDICAL_GROUNDS("MEDICAL GROUNDS", "Medical Grounds", HardshipReviewDetailType.INCOME.getType()),
    SUSPENDED_WORK("SUSPENDED WORK", "Suspended from work", HardshipReviewDetailType.INCOME.getType()),
    OTHER_INC("OTHER INC", "Other", HardshipReviewDetailType.INCOME.getType());

    @JsonPropertyDescription("Denied Income detail codes that are valid")
    private final String code;
    private final String description;
    private final String type;

    public static DeniedIncomeDetailCode getFrom(String code) {
        if (StringUtils.isBlank(code)) return null;

        return Stream.of(DeniedIncomeDetailCode.values())
                .filter(didCode -> didCode.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Denied income detail code: %s does not exist.", code)));
    }

}
