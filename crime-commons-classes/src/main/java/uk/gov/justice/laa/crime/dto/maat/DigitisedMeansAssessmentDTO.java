package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DigitisedMeansAssessmentDTO extends GenericDTO {

    private Long id;
    private Long maatId;
    private Date dateCreated;
    private Date originalEmailDate;

}
