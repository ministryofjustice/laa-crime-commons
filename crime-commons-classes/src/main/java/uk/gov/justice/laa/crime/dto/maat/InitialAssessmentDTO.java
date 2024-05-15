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
public class InitialAssessmentDTO extends GenericDTO {

    private Long id;
    private Long criteriaId;
    private Date assessmentDate;
    private String otherBenefitNote;
    private String otherIncomeNote;
    private Double totalAggregatedIncome;
    private Double adjustedIncomeValue;
    private String notes;
    private Double lowerThreshold;
    private Double upperThreshold;
    private String result;
    private String resultReason;
    private AssessmentStatusDTO assessmnentStatusDTO;
    private NewWorkReasonDTO newWorkReason;
    private ReviewTypeDTO reviewType;
    private SupplierDTO supplierDTO;
    private Collection<AssessmentSectionSummaryDTO> sectionSummaries;
    private Collection<ChildWeightingDTO> childWeightings;

}