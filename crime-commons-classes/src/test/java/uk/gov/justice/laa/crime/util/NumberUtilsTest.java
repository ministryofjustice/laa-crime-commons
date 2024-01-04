package uk.gov.justice.laa.crime.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

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
    void givenNull_whenToDoubleIsInvoked_NullIsReturned() {
        assertThat(NumberUtils.toDouble(null)).isNull();
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
