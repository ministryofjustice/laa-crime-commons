package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HRSolicitorsCostsDTO extends GenericDTO {

    private BigDecimal solicitorRate;
    private Double solicitorHours;
    private BigDecimal solicitorVat;
    private BigDecimal solicitorDisb;
    private BigDecimal solicitorEstimatedTotalCost;

}
