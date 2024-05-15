package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AssessmentStatusDTO extends GenericDTO {

    public static final String COMPLETE = "COMPLETE";
    public static final String INCOMPLETE = "IN PROGRESS";
    public static final String EVIDENCE = "EVIDENCE";

    private String status;
    private String description;

}
