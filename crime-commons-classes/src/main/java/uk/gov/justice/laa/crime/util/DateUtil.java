package uk.gov.justice.laa.crime.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    public static Date toDate(final LocalDateTime source) {
        return source != null ? Date.from(source.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    public static LocalDateTime toLocalDateTime(final Date date) {
        return (date != null) ? LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) : null;
    }

    public static Timestamp toTimeStamp(final LocalDateTime source) {
        return (source != null) ? Timestamp.valueOf(source) : null;
    }

    public static LocalDateTime getLocalDateTime(final String date) {
        return date != null ? LocalDate.parse(date).atTime(0, 0) : null;
    }

    public static LocalDate parse(final String date) {
        return date != null ? LocalDate.parse(date) : null;
    }

    public static LocalDate parseLocalDate(final LocalDateTime date) {
        return date != null ? date.toLocalDate() : null;
    }

    public static String getLocalDateString(final LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")) : null;
    }

    public static LocalDateTime convertDateToDateTime(LocalDate date) {
        return (date != null) ? date.atTime(0, 0) : null;
    }

}