package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DrcSupplierDTO extends GenericDTO {

    private static final long serialVersionUID = 214057823559905340L;

    private long id;
    private String name;
    private String status;
    private AddressDTO address;
}
