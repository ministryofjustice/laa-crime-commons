package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class HRReasonDTO {

    private Long id;
    private String reason;
    private Boolean accepted;

}
