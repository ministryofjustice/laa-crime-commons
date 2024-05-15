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
public class DrcConversationDTO extends GenericDTO {
    private long rep_id;
    private DrcDTO drcDTO;
    private Collection<DrcProcessDTO> drcProcessStages;

}
