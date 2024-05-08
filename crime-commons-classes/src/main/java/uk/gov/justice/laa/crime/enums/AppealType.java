package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum AppealType {

    ASE ("ASE", "Appeal against sentence"),
    ACN ("ACN", "Appeal against conviction"),
    ACS ("ACS", "Appeal against conviction and sentence");

    @NotNull
    @JsonValue
    @JsonPropertyDescription("Specifies the appeal type code")
    private final String code;
    private final String description;

    public static AppealType getFrom(String code) {
        if (StringUtils.isBlank(code)) return null;

        return Stream.of(AppealType.values())
                .filter(appealOutcome -> appealOutcome.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Appeal Type with value: %s does not exist.", code)));
    }

}
