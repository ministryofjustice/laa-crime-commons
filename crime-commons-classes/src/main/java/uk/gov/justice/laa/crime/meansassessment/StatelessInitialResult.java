package uk.gov.justice.laa.crime.meansassessment;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.justice.laa.crime.enums.InitAssessmentResult;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
@Builder
public class StatelessInitialResult {
    private final InitAssessmentResult result;
    private final BigDecimal lowerThreshold;
    private final BigDecimal upperThreshold;
    private final boolean fullAssessmentPossible;
    private final BigDecimal adjustedIncomeValue;
    private final BigDecimal totalAggregatedIncome;

    public String getResultReason() {
        return result.getReason();
    }
}

