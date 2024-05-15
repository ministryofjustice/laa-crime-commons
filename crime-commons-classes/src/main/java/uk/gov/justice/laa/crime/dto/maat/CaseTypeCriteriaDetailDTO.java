package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CaseTypeCriteriaDetailDTO extends GenericDTO {

    private String catyCaseType;
    private Long id;
    private Double applicantValue;
    private Double partnerValue;
    private String applicantFreq;
    private String partnerFreq;

}
