package uk.gov.justice.laa.crime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum RepOrderStatus {

    CURR("CURR", "Current", false, true, 1),
    FI("FI", "Awaiting FI", false, true, 2),
    ERR("ERR", "Created in Error", true, false, 3),
    SUSP("SUSP", "Suspended", false, true, 4),
    NOT_SENT_FOR_TRIAL("NOT SENT FOR TRIAL", "Not sent for trial", false, true, 5);

    private final String code;
    private final String description;
    private final boolean removeContribs;
    private final boolean updateAllowed;
    private final int sequence;

    public static RepOrderStatus getFrom(String code) {
        if (StringUtils.isBlank(code)) return null;

        return Stream.of(RepOrderStatus.values())
                .filter(crownCourtOutcome -> crownCourtOutcome.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Rep Order Status with value: %s does not exist.", code)));
    }
}
