package uk.gov.justice.laa.crime.meansassessment;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.justice.laa.crime.enums.FullAssessmentResult;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
@Builder
public class StatelessFullResult {
    private final FullAssessmentResult result;
    private final BigDecimal disposableIncome;
    private final BigDecimal adjustedLivingAllowance;
    private final BigDecimal totalAnnualAggregatedExpenditure;
    private final BigDecimal eligibilityThreshold;

    public String getResultReason() {
        return result.getReason();
    }
}

