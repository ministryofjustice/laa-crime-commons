package uk.gov.justice.laa.crime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CrownCourtOutcome {

    ABANDONED("ABANDONED", CrownCourtOutcomeType.TRIAL, "Abandoned"),
    AQUITTED("AQUITTED", CrownCourtOutcomeType.TRIAL, "Acquitted"),
    CONVICTED("CONVICTED", CrownCourtOutcomeType.TRIAL, "Convicted"),
    DISMISSED("DISMISSED", CrownCourtOutcomeType.TRIAL, "Dismissed"),
    PART_CONVICTED("PART CONVICTED", CrownCourtOutcomeType.TRIAL, "Partially Convicted"),
    PART_SUCCESS("PART SUCCESS", CrownCourtOutcomeType.APPEAL, "Partially Successful"),
    SUCCESSFUL("SUCCESSFUL", CrownCourtOutcomeType.APPEAL, "Successful"),
    UNSUCCESSFUL("UNSUCCESSFUL", CrownCourtOutcomeType.APPEAL, "Unsuccessful");

    private final String code;
    private final String type;
    private final String description;

    public static CrownCourtOutcome getFrom(String code) {
        if (StringUtils.isBlank(code)) return null;

        return Stream.of(CrownCourtOutcome.values())
                .filter(crownCourtOutcome -> crownCourtOutcome.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Crown Court Outcome with value: %s does not exist.", code)));
    }

    private static class CrownCourtOutcomeType {
        private static final String TRIAL = "TRIAL";
        private static final String APPEAL = "APPEAL";
    }
}
