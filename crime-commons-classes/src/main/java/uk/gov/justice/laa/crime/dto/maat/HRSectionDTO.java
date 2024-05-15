package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Collection;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HRSectionDTO extends GenericDTO {

    private HRDetailTypeDTO detailType;
    private Collection<HRDetailDTO> detail;
    private BigDecimal applicantAnnualTotal;
    private BigDecimal partnerAnnualTotal;
    private BigDecimal annualTotal;

}
