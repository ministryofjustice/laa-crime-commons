package uk.gov.justice.laa.crime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import uk.gov.justice.laa.crime.exception.ValidationException;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum CrownCourtAppealOutcome {

    SUCCESSFUL("SUCCESSFUL"),
    UNSUCCESSFUL("UNSUCCESSFUL"),
    PART_SUCCESS("PART SUCCESS");

    private String value;

    public static boolean isAppeal(String outcome) {

        return Stream.of(CrownCourtAppealOutcome.values())
                .anyMatch(appOut -> appOut.getValue().equalsIgnoreCase(notEmpty(outcome)));
    }


    private static String notEmpty(String outcome) {

        return Optional.ofNullable(outcome).orElseThrow(
                () -> new ValidationException("Crown Court appeal outcome can't be empty."));
    }

    public static CrownCourtAppealOutcome getFrom(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return Stream.of(CrownCourtAppealOutcome.values())
                .filter(appealOutcome -> appealOutcome.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Appeal Outcome with value: %s does not exist.", value)));
    }
}
