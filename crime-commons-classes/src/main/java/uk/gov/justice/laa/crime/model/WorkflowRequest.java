package uk.gov.justice.laa.crime.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.justice.laa.crime.dto.maat.ApplicationDTO;
import uk.gov.justice.laa.crime.dto.maat.UserDTO;
import uk.gov.justice.laa.crime.enums.CourtType;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowRequest {
    private UserDTO userDTO;
    private ApplicationDTO applicationDTO;
    private boolean isC3Enabled;
    private CourtType courtType;
}
