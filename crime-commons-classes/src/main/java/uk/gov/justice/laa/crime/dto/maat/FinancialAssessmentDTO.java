package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FinancialAssessmentDTO extends GenericDTO {
    private Long id;
    private Long criteriaId;
    private InitialAssessmentDTO initial;
    private FullAssessmentDTO full;
    private HardshipOverviewDTO hardship;
    private IncomeEvidenceSummaryDTO incomeEvidence;
    private Boolean fullAvailable;
    private Long usn;

}
