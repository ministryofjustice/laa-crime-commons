package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ThirdPartyOwnerDTO extends GenericDTO {

    private Long id;
    private Long ropdId;
    private String ownerName;
    private String ownerRelation;
    private String otherRelation;

    private ThirdPartyTypeDTO thirdPartyType;

}
