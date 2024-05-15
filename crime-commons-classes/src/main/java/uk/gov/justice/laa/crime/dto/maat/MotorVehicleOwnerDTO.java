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
public class MotorVehicleOwnerDTO extends GenericDTO {

    private static final long serialVersionUID = -6525468902833698008L;

    private Long id;
    private Long repId;
    private Boolean noVehicleDeclared;
    private Boolean available;

    private Collection<VehicleRegistrationMarkDTO> vehicleRegistrationMarkDTOs;
}
