package uk.gov.justice.laa.crime.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.justice.laa.crime.enums.NewWorkReason;
import uk.gov.justice.laa.crime.enums.orchestration.Action;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserValidationDTO {
    private String username;
    private Action action;
    private NewWorkReason newWorkReason;
    private String sessionId;
}
