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
public class PartnerDTO extends GenericDTO {
    private String firstName;
    private String surname;
    private String nationaInsuranceNumber;
    private Date dateOfBirth;

}