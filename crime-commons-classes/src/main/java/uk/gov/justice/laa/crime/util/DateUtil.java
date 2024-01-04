package uk.gov.justice.laa.crime.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    public static Date toDate(LocalDateTime source) {
        if (source != null) {
            return Date.from(
                    source.atZone(ZoneId.systemDefault())
                            .toInstant());
        } else {
            return null;
        }
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date != null) {
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }
        return null;
    }

    public static Timestamp toTimeStamp(LocalDateTime source) {
        if (source != null) {
            return Timestamp.valueOf(source);
        }
        return null;
    }
}