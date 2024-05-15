package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AssessmentCriteriaChildWeightingDTO extends GenericDTO {

    private Long id;
    private Long assessmentCriteriaId;
    private Integer lowerAge;
    private Integer upperAge;
    private Double weightingFactor;

}
