package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.justice.laa.crime.orchestration.dto.maat.GenericDTO;
import uk.gov.justice.laa.crime.orchestration.dto.maat.HRProgressActionDTO;
import uk.gov.justice.laa.crime.orchestration.dto.maat.HRProgressResponseDTO;

import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HRProgressDTO extends GenericDTO {

    private Long id;
    private HRProgressActionDTO progressAction;
    private HRProgressResponseDTO progressResponse;
    private Date dateRequested;
    private Date dateRequired;
    private Date dateCompleted;

}
