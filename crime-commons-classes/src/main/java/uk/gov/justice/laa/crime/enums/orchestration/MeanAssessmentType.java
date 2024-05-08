package uk.gov.justice.laa.crime.enums.orchestration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeanAssessmentType {

    MEANS_INIT("MEANS_INIT"),
    MEANS_FULL("MEANS_FULL");
    private final String code;
}
