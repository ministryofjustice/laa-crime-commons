package uk.gov.justice.laa.crime.enums.contribution;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CorrespondenceStatusTest {

    @Test
    void givenValidValue_whenGetFromIsInvoked_thenValidStatusIsReturned() {
        assertThat(CorrespondenceStatus.getFrom("appealCC"))
                .isEqualTo(CorrespondenceStatus.APPEAL_CC);
    }

    @Test
    void givenBlankString_whenGetFromIsInvoked_thenNullIsReturned() {

        assertThat(CorrespondenceStatus.getFrom(""))
                .isNull();
    }

    @Test
    void givenInvalidValue_whenGetFromIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> CorrespondenceStatus.getFrom("FLIBBLE"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
