package uk.gov.justice.laa.crime.orchestration.enums.cat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestSource {

    CREATE_APPLICATION("create_application"),
    PASSPORT_IOJ("passport_ioj"),
    MEANS_ASSESSMENT("means_assessment"),
    HARDSHIP("hardship"),
    CROWN_COURT("crown_court"),
    CAPITAL_AND_EQUITY("capital_and_equity");

    private final String code;
}
