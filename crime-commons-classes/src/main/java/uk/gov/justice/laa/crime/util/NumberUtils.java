package uk.gov.justice.laa.crime.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtils {

    public static Integer toInteger(Long value) {
        return (value != null) ? value.intValue() : null;
    }

    public static Double toDouble(Integer input) {
        return (input != null) ? input.doubleValue() : null;
    }

    public static Double toDouble(BigDecimal input) {
        return (input != null) ? input.doubleValue() : null;
    }
}
