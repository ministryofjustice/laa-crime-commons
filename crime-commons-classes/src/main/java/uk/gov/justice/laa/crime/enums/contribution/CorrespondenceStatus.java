package uk.gov.justice.laa.crime.enums.contribution;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CorrespondenceStatus {
    APPEAL_CC("appealCC"),
    CDS15("cds15"),
    REASS("re-ass"),
    NONE("none");

    private static final String EXCEPTION_MESSAGE = "Correspondence status with value: %s does not exist";

    @NotNull
    @JsonValue
    private final String status;

    public static CorrespondenceStatus getFrom(String status) {
        if (StringUtils.isBlank(status)) {
            return null;
        }

        return Stream.of(CorrespondenceStatus.values())
                .filter(currentStatus -> currentStatus.status.equals(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(EXCEPTION_MESSAGE, status)));
    }

}