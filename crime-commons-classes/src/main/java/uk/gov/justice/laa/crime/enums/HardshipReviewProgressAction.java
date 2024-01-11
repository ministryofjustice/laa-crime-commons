package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum HardshipReviewProgressAction {

    FURTHER_INFO("FURTHER INFO", "Further Information requested"),
    ORIG_APP_REQUESTED("ORIG APP REQUESTED", "Original Appliction Requested"),
    ORIG_APP_RETURNED("ORIG APP RETURNED", "Original Application returned to HMCS"),
    SOLICITOR_INFORMED("SOLICITOR INFORMED", "Solicitor Informed"),
    ADDITIONAL_EVIDENCE("ADDITIONAL EVIDENCE", "Additional Evidence Requested"),
    REJECTED_APP("REJECTED APP", "Rejected application"),
    OTHER("OTHER", "Other");

    @JsonPropertyDescription("This will have the hardship review progress action")
    @JsonValue
    private final String action;
    private final String description;

    public static HardshipReviewProgressAction getFrom(String action) {
        if (StringUtils.isBlank(action)) return null;

        return Stream.of(HardshipReviewProgressAction.values())
                .filter(hrpa -> hrpa.action.equals(action))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("hardship review progress action with type: %s does not exist.", action)));
    }

}
