package uk.gov.justice.laa.crime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum NewWorkReason {


    PBI("PBI", NewWorkReasonType.ASS, "Crown Court Section 51"),
    PCI("PCI", NewWorkReasonType.ASS, "Crown Court either way offence"),
    FMA("FMA", NewWorkReasonType.ASS, "First Means Assessment"),
    PAI("PAI", NewWorkReasonType.ASS, "Previous Assessment was Incorrect"),
    CFC("CFC", NewWorkReasonType.ASS, "Change in Financial Circumstances"),
    CPS("CPS", NewWorkReasonType.ASS, "Change in Solicitor"),
    HR("HR", NewWorkReasonType.ASS, "Hardship Review (NCT only)"),
    NEW("NEW", NewWorkReasonType.HARDIOJ, "New"),
    PRI("PRI", NewWorkReasonType.HARDIOJ, "Previous Record Incorrect"),
    JR("JR", NewWorkReasonType.HARDIOJ, "Judicial Review"),
    EVI("EVI", NewWorkReasonType.ASS, "Income Evidence Differs from Declaration"),
    INF("INF", NewWorkReasonType.ASS, "Re-assessment Following New Information"),
    CSP("CSP", NewWorkReasonType.ASS, "Change in Partner Status");

    private String code;
    private String type;
    private String description;

    public static NewWorkReason getFrom(String code) {
        if (StringUtils.isBlank(code)) return null;

        return Stream.of(NewWorkReason.values())
                .filter(newWorkReason -> newWorkReason.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("New Work Reason with value: %s does not exist.", code)));
    }

    private static class NewWorkReasonType {
        private static final String ASS = "ASS";
        private static final String HARDIOJ = "HARDIOJ";
    }
}
