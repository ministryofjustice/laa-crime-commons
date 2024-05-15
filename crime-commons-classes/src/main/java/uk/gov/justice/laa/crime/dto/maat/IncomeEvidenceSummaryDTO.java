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
public class IncomeEvidenceSummaryDTO extends GenericDTO {
    private static final long serialVersionUID = 1L;

    private Date evidenceDueDate;
    private Date evidenceReceivedDate;
    private Date upliftAppliedDate;
    private Date upliftRemovedDate;
    private Date firstReminderDate;
    private Date secondReminderDate;
    private String incomeEvidenceNotes;
    private Collection<EvidenceDTO> applicantIncomeEvidenceList;
    private Collection<EvidenceDTO> partnerIncomeEvidenceList;
    private Collection<ExtraEvidenceDTO> extraEvidenceList;
    private Boolean enabled;
    private Boolean upliftsAvailable;

}

