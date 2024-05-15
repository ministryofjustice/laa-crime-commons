package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LscTransferDTO extends AreaTransferDetailsDTO {
    private Date dateLscReceived;
    private Date dateLscReturned;
    private UserDTO lscReceivedBy;
    private UserDTO lscReturnedBy;

}
