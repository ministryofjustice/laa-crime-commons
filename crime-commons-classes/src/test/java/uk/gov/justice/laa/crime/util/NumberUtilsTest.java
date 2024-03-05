package uk.gov.justice.laa.crime.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NumberUtilsTest {

    @Test
    void givenNullLong_whenToIntegerIsInvoked_thenNullIsReturned() {
        assertThat(NumberUtils.toInteger(null)).isNull();
    }

    @Test
    void givenLong_whenToIntegerIsInvoked_thenIntValueIsReturned() {
        assertThat(NumberUtils.toInteger(500L)).isEqualTo(500);
    }

    @Test
    void givenBigDecimal_whenToDoubleIsInvoked_thenValidCurrencyIsReturned() {
        assertThat(NumberUtils.toDouble(BigDecimal.TEN)).isEqualTo(10.0);
    }

    @Test
    void givenNullInteger_whenToDoubleIsInvoked_NullIsReturned() {
        Integer empty = null;
        assertThat(NumberUtils.toDouble(empty)).isNull();
    }

    @Test
    void givenNull_whenToDoubleIsInvoked_NullIsReturned() {
        BigDecimal empty = null;
        assertThat(NumberUtils.toDouble(empty)).isNull();
    }

    @Test
    void givenAValidInput_whenToDoubleIsInvoked_ValidCurrencyIsReturned() {
        assertThat(NumberUtils.toDouble(1)).isNotNull();
    }

    @Test
    void testCurrencyUtilConstructorIsPrivate() throws NoSuchMethodException {
        Assertions.assertThat(NumberUtils.class.getDeclaredConstructors()).hasSize(1);
        Constructor<NumberUtils> constructor = NumberUtils.class.getDeclaredConstructor();
        Assertions.assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
    }
}
