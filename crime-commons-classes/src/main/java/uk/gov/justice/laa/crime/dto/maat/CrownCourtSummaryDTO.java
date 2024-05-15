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
public class CrownCourtSummaryDTO extends GenericDTO {
    public static final String REP_ORDER_DECISION_GRANTED = "Granted";
    public static final String REP_TYPE_CC_ONLY = "Crown Court Only";

    private Long ccRepId;
    private SysGenString ccRepType;
    private Date ccRepOrderDate;
    private Date sentenceOrderDate;
    private Date ccWithDrawalDate;
    private SysGenString repOrderDecision;
    private Boolean inPrisoned;
    private OutcomeDTO ccOutcome;
    private OutcomeDTO ccAppealOutcome;
    private Boolean benchWarrantyIssued;
    private EvidenceFeeDTO evidenceProvisionFee;
    private Collection<OutcomeDTO> outcomeDTOs;

}
