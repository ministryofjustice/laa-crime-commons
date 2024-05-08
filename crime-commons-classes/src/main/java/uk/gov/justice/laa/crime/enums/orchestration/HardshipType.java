package uk.gov.justice.laa.crime.enums.orchestration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HardshipType {

    MCHARDSHIP("MCHARDSHIP"),
    CCHARDSHIP("CCHARDSHIP");

    private final String code;

}
