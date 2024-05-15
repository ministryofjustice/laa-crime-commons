package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.justice.laa.crime.orchestration.dto.maat.DrcConversationDTO;
import uk.gov.justice.laa.crime.orchestration.dto.maat.GenericDTO;

import java.util.Collection;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DrcSummaryDTO extends GenericDTO {

    private long repId;
    private Collection<DrcConversationDTO> conversations;

}
