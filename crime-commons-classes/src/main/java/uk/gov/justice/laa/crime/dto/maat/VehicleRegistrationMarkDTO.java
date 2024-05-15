package uk.gov.justice.laa.crime.dto.maat;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VehicleRegistrationMarkDTO extends GenericDTO {

    private static final long serialVersionUID = -2856620006134321801L;

    private Long movId;
    private String VehicleRegistrationMark;

}
