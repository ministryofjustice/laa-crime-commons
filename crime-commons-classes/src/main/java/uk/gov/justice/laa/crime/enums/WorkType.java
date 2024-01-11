package uk.gov.justice.laa.crime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkType {

    INITIAL_ASSESSMENT("Initial Assessment"),
    FULL_MEANS_TEST("Full Means Test"),
    PASSPORTED("Passported"),
    HARDSHIP_REVIEW_MAGISTRATE("Hardship Review - Magistrate"),
    HARDSHIP_REVIEW_CROWN_COURT("Hardship Review - Crown Court"),
    IOJ_APPEAL("IoJ Appeal");

    private final String description;
}
