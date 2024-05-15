package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Collection;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CapitalEquityDTO extends GenericDTO {
    public static final String RESIDENTIAL_STATUS_OWNER = "OWNER";
    public static final String RESIDENTIAL_STATUS_NO_FIXED_ABODE = "NOFIXABODE";

    private Boolean available;
    private Boolean noCapitalDeclared;
    private Boolean suffientVeriToCoverCase;
    private Boolean verifiedEquityToCoverCase;
    private Boolean suffientDeclToCoverCase;
    private Boolean declaredEquityToCoverCase;
    private BigDecimal totalCapital;
    private BigDecimal totalEquity;
    private BigDecimal totalCapitalAndEquity;
    private Collection<EquityDTO> equityObjects;
    private Collection<CapitalPropertyDTO> capitalProperties;
    private Collection<CapitalOtherDTO> capitalOther;
    private CapitalEvidenceSummaryDTO capitalEvidenceSummary;

    private MotorVehicleOwnerDTO motorVehicleOwnerDTO;

    private Long usn;
}
