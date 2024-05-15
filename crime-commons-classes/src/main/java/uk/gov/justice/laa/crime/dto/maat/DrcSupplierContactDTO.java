package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DrcSupplierContactDTO extends GenericDTO {
    long id;
    long supplierId;
    String firstName;
    String lastName;
    String title;
    String telephone;
    String status;
    String description;
}
