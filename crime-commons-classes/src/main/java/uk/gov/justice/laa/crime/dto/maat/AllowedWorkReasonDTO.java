package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AllowedWorkReasonDTO extends GenericDTO {
    private Collection<NewWorkReasonDTO> passportWorkReason;
    private Collection<NewWorkReasonDTO> meansAssessmentWorkReason;
    private Collection<NewWorkReasonDTO> magsCourtHardshipWorkReason;
    private Collection<NewWorkReasonDTO> crownCourtHardshipWorkReason;
    private Collection<NewWorkReasonDTO> eligibilityWorkReason;
    private Collection<NewWorkReasonDTO> iojAppealWorkReason;
}
