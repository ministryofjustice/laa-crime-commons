package uk.gov.justice.laa.crime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CrownCourtOutcome {

    ABANDONED("ABANDONED", CrownCourtOutcomeType.TRIAL.getType(), "Abandoned"),
    AQUITTED("AQUITTED", CrownCourtOutcomeType.TRIAL.getType(), "Acquitted"),
    CONVICTED("CONVICTED", CrownCourtOutcomeType.TRIAL.getType(), "Convicted"),
    DISMISSED("DISMISSED", CrownCourtOutcomeType.TRIAL.getType(), "Dismissed"),
    PART_CONVICTED("PART CONVICTED", CrownCourtOutcomeType.TRIAL.getType(), "Partially Convicted"),
    PART_SUCCESS("PART SUCCESS", CrownCourtOutcomeType.APPEAL.getType(), "Partially Successful"),
    SUCCESSFUL("SUCCESSFUL", CrownCourtOutcomeType.APPEAL.getType(), "Successful"),
    UNSUCCESSFUL("UNSUCCESSFUL", CrownCourtOutcomeType.APPEAL.getType(), "Unsuccessful");

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
}
