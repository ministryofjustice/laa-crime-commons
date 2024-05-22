package uk.gov.justice.laa.crime.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MaskingpatternLayoutTest {

    MaskingPatternLayout maskingPatternLayout = new MaskingPatternLayout();

    @Test
    void givenAValidMask_whenMaskMessageIsInvoked_thenShouldMaskMessage() {
        maskingPatternLayout.addMaskPattern("id=(\\d+)");
        assertThat(maskingPatternLayout.maskMessage(getMessage())).contains(getExpectedMessage());
    }

    @Test
    void givenAValidMultipleMask_whenMaskMessageIsInvoked_thenShouldMaskMessage() {
        maskingPatternLayout.addMaskPattern("id=(\\d+)");
        maskingPatternLayout.addMaskPattern("upliftApplied=(\\w+)");
        assertThat(maskingPatternLayout.maskMessage(getMessage())).contains(getExpectedMessageWithMask());
    }

    @Test
    void givenAInvalidMask_whenMaskMessageIsInvoked_thenShouldNotMaskMessage() {
        maskingPatternLayout.addMaskPattern("invalid=(\\d+)");
        assertThat(maskingPatternLayout.maskMessage(getMessage())).contains(getMessage());
    }

    private  String getMessage() {
        return "[ContributionsSummaryDTO(id=239272884, monthlyContributions=0,upfrontContributions=0, " +
                "basedOn=null, upliftApplied=N, effectiveDate=2024-03-18,calcDate=2024-03-18, " +
                "fileName=null, dateSent=null, dateReceived=null]";
    }

    private  String getExpectedMessage() {
        return "[ContributionsSummaryDTO(id=*********, monthlyContributions=0,upfrontContributions=0, " +
                "basedOn=null, upliftApplied=N, effectiveDate=2024-03-18,calcDate=2024-03-18, " +
                "fileName=null, dateSent=null, dateReceived=null]";
    }

    private  String getExpectedMessageWithMask() {
        return "[ContributionsSummaryDTO(id=*********, monthlyContributions=0,upfrontContributions=0, " +
                "basedOn=null, upliftApplied=*, effectiveDate=2024-03-18,calcDate=2024-03-18, " +
                "fileName=null, dateSent=null, dateReceived=null]";
    }

}
