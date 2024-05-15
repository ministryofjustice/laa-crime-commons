package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AssessmentDetailDTO extends GenericDTO {

    private Long id;
    private Long criteriaDetailsId;
    private Double applicantAmount;
    private Double partnerAmount;
    private FrequenciesDTO applicantFrequency;
    private FrequenciesDTO partnerFrequency;
    private String description;
    private String detailCode;

}
