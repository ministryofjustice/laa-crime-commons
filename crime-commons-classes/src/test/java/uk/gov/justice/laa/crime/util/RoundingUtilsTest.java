package uk.gov.justice.laa.crime.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RoundingUtilsTest {

    @Test
    void testRoundingUtilConstructorIsPrivate() throws NoSuchMethodException {
        assertThat(RoundingUtils.class.getDeclaredConstructors()).hasSize(1);
        Constructor<RoundingUtils> constructor = RoundingUtils.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
    }
}
