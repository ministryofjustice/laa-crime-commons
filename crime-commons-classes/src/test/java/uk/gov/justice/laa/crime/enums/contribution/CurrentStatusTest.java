package uk.gov.justice.laa.crime.enums.contribution;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.enums.contribution.CurrentStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CurrentStatusTest {

    @Test
    void givenValidValue_whenGetFromIsInvoked_thenValidStatusIsReturned() {
        assertThat(CurrentStatus.getFrom("IN PROGRESS")).isEqualTo(CurrentStatus.IN_PROGRESS);
    }

    @Test
    void givenBlankString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(CurrentStatus.getFrom("")).isNull();
    }

    @Test
    void givenInvalidValue_whenGetFromIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> CurrentStatus.getFrom("FLIBBLE")).isInstanceOf(IllegalArgumentException.class);
    }
}
