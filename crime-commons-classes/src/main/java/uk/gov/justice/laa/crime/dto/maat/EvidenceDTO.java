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
public class EvidenceDTO extends GenericDTO {
    private static final long serialVersionUID = 1L;

    private Long id;
    private EvidenceTypeDTO evidenceTypeDTO;
    private String otherDescription;
    private Date dateReceived;

}
