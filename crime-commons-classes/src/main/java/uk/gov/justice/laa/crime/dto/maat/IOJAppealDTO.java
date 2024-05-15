package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class IOJAppealDTO extends GenericDTO {
    private Long iojId;
    private Long cmuId;
    private Date receivedDate;
    private Date decisionDate;
    private String appealSetUpResult;
    private String appealDecisionResult;
    private String notes;

    private IOJDecisionReasonDTO appealReason;
    private AssessmentStatusDTO assessmentStatusDTO;
    private NewWorkReasonDTO newWorkReasonDTO;

}