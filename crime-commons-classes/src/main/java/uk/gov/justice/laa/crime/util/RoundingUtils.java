package uk.gov.justice.laa.crime.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoundingUtils {

    public static BigDecimal setStandardScale(BigDecimal valueToScale) {
        return valueToScale.setScale(2, RoundingMode.HALF_UP);
    }
}
