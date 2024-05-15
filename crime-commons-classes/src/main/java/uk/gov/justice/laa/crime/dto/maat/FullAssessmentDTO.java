package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FullAssessmentDTO extends GenericDTO {
    private Long criteriaId;
    private Date assessmentDate;
    private String assessmentNotes;
    private Collection<AssessmentSectionSummaryDTO> sectionSummaries;
    private Double adjustedLivingAllowance;
    private String otherHousingNote;
    private Double totalAggregatedExpense;
    private Double totalAnnualDisposableIncome;
    private Double threshold;
    private String result;
    private String resultReason;
    private AssessmentStatusDTO assessmnentStatusDTO;

}
