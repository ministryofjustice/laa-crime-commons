package uk.gov.justice.laa.crime.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DateUtilTest {

    @Test
    void givenAEmptyStringDate_whenToDateIsInvoked_thenReturnNull() {
        assertThat(DateUtil.toDate(null)).isNull();
    }

    @Test
    void givenAValidLocalDateTime_whenToDateIsInvoked_thenCorrectDateIsReturned() {
        LocalDateTime dateModified = LocalDateTime.now();
        assertThat(DateUtil.toDate(dateModified)).isNotNull();
    }

    @Test
    void givenAValidDate_whenToLocalDateTimeIsInvoked_thenCorrectDateTimeIsReturned() {
        Calendar instance = Calendar.getInstance();
        instance.set(2023, Calendar.NOVEMBER, 29);
        Date date = instance.getTime();
        assertThat(DateUtil.toLocalDateTime(date).toLocalDate())
                .isEqualTo(LocalDateTime.of(2023, 11, 29, 0, 0, 0).toLocalDate());
    }

    @Test
    void givenANullDate_whenToLocalDateTimeIsInvoked_thenNullIsReturned() {
        assertThat(DateUtil.toLocalDateTime(null))
                .isNull();
    }

    @Test
    void givenAValidDateTime_whenToTimeStampIsInvoked_thenCorrectDateTimeIsReturned() {
        LocalDateTime dateModified = LocalDateTime.now();
        assertThat(DateUtil.toTimeStamp(dateModified)).isNotNull();
    }

    @Test
    void givenANullDate_whenToTimeStampIsInvoked_thenNullIsReturned() {
        assertThat(DateUtil.toTimeStamp(null))
                .isNull();
    }
}
