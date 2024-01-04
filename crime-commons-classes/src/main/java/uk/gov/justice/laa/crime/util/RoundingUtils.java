package uk.gov.justice.laa.crime.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundingUtils {

    private RoundingUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static BigDecimal setStandardScale(BigDecimal valueToScale) {
        return valueToScale.setScale(2, RoundingMode.HALF_UP);
    }
}
