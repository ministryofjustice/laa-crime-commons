package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AddressDTO extends GenericDTO {
    private Long id;
    private String line1;
    private String line2;
    private String line3;
    private String city;
    private String postCode;
    private String county;
    private String country;
}