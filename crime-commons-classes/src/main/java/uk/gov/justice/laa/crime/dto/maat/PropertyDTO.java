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
public class PropertyDTO extends GenericDTO {
    private Long id;
    private AddressDTO addressDto;
    private ResidentialStatusDTO residentialStatus;
    private PropertyTypeDTO type;
    private Double percentageOwnedApplicant;
    private Double percentageOwnedPartner;
    private String bedrooms;
    private BigDecimal declaredMarketValue;
    private BigDecimal declaredMortgageCharges;
    private BigDecimal verifiedMarketValue;
    private BigDecimal verifiedMortgageCharges;
    private ResidentialStatusDTO verifyResidentialStatus;
    private Boolean tenantInPlace;
    private BigDecimal verifiedEquityAmount;
    private BigDecimal declaredEquityAmount;
    private Collection<ThirdPartyOwnerDTO> thirdPartyOwners;

}
