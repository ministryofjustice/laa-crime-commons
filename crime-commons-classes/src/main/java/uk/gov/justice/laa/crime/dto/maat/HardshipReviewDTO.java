package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HardshipReviewDTO extends GenericDTO {

    private Long id;
    private SupplierDTO supplier;
    private NewWorkReasonDTO newWorkReason;
    private Long cmuId;
    private String reviewResult;
    private Date reviewDate;
    private String notes;
    private String decisionNotes;
    // Spelling mistake in MAAT
    private HRSolicitorsCostsDTO solictorsCosts;
    private BigDecimal disposableIncome;
    private BigDecimal disposableIncomeAfterHardship;
    private Collection<HRSectionDTO> section;
    private Collection<HRProgressDTO> progress;
    // Spelling mistake in MAAT
    private AssessmentStatusDTO asessmentStatus;
}
