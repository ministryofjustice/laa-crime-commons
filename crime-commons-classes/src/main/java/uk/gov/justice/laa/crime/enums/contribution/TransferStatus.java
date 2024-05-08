package uk.gov.justice.laa.crime.enums.contribution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum TransferStatus {
    ERRATA("ERATA"),
    QUARANTINE("QUARANTINE"),
    REQUESTED("REQUESTED"),
    SENT("SENT"),
    MANUAL("MANUAL"),
    RECEIVED("RECEIVED");

    private static final String EXCEPTION_MESSAGE = "Transfer status with value: %s does not exist";

    private final String value;

    public static TransferStatus getFrom(String value) {
        if (StringUtils.isBlank(value)) return null;

        return Stream.of(TransferStatus.values())
                .filter(transferStatus -> transferStatus.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(EXCEPTION_MESSAGE, value)));
    }

}
