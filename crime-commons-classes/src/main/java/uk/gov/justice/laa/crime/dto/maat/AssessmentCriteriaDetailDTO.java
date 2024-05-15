package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AssessmentCriteriaDetailDTO extends GenericDTO {

    private Long id;
    private Long assessmentCriteriaId;
    private String description;
    private String section;
    private Integer sequence;
    private String asdeDetailCode;

    private Collection<CaseTypeCriteriaDetailDTO> caseTypeCriteriaDetail;
    private Collection<FrequenciesDTO> freqDetails;
}
