package uk.gov.justice.laa.crime.enums.meansassessment;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgeRange {
    ZERO_TO_ONE("0-1", 0, 1),
    TWO_TO_FOUR("2-4", 2, 4),
    FIVE_TO_SEVEN("5-7", 5, 7),
    EIGHT_TO_TEN("8-10", 8, 10),
    ELEVEN_TO_TWELVE("11-12", 11, 12),
    THIRTEEN_TO_FIFTEEN("13-15", 13, 15),
    SIXTEEN_TO_EIGHTEEN("16-18", 16, 18);

    @JsonValue
    private final String value;
    private final int lowerLimit;
    private final int upperLimit;
}
