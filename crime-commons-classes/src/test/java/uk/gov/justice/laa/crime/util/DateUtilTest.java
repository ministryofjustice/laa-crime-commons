package uk.gov.justice.laa.crime.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
    @Test
    void givenAValidLocalDate_whenGetLocalDateTimeIsInvoked_thenReturnDateTime() {
        String dateModified = "2022-12-09";
        assertThat(DateUtil.getLocalDateTime(dateModified)).isNotNull();
    }

    @Test
    void givenAEmptyLocalDate_whenGetLocalDateTimeIsInvoked_thenReturnNull() {
        assertThat(DateUtil.getLocalDateTime(null)).isNull();
    }

    @Test
    void givenAEmptyStringDate_whenParseIsInvoked_thenReturnNull() {
        assertThat(DateUtil.parse(null)).isNull();
    }

    @Test
    void givenAValidDate_whenParseIsInvoked_thenReturnDate() {
        String dateModified = "2023-01-01";
        assertThat(DateUtil.parse(dateModified)).isNotNull();
    }

    @Test
    void givenAEmptyLocalDate_whenParseIsInvoked_thenReturnNull() {
        assertThat(DateUtil.parseLocalDate(null)).isNull();
    }

    @Test
    void givenAValidLocalDate_whenParseIsInvoked_thenReturnDate() {
        LocalDateTime dateModified = LocalDateTime.now();
        assertThat(DateUtil.parseLocalDate(dateModified)).isNotNull();
    }

    @Test
    void givenAValidLocalDate_whenConvertDateToDateTimeIsInvoked_thenReturnDateTime() {
        LocalDate dateModified = LocalDate.now();
        assertThat(DateUtil.convertDateToDateTime(dateModified)).isNotNull();
    }

    @Test
    void givenAEmptyLocalDate_whenConvertDateToDateTimeIsInvoked_thenReturnNull() {
        assertThat(DateUtil.convertDateToDateTime(null)).isNull();
    }

    @Test
    void givenNullLocalDate_whenGetLocalDateStringIsInvoked_thenReturnNull() {
        assertThat(DateUtil.getLocalDateString(null)).isNull();
    }

    @Test
    void givenAValidLocalDate_whenGetLocalDateStringIsInvoked_thenReturnCorrectString() {
        assertThat(DateUtil.getLocalDateString(LocalDate.of(2023, 10, 6))).isEqualTo("06-Oct-2023");
    }

    @Test
    void givenAValidLocalDateTime_whenToZonedDateTimeIsInvoked_thenReturnZonedDateTime() {
        ZonedDateTime expected = ZonedDateTime.parse("2011-12-03T10:15:30.342+00:00");
        ZonedDateTime actual = DateUtil.toZonedDateTime(LocalDateTime.parse("2011-12-03T10:15:30.342"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void givenNullLocalDateTime_whenToZonedDateTimeIsInvoked_thenReturnNull() {
        assertThat(DateUtil.toZonedDateTime(null)).isNull();
    }
}
