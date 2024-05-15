package uk.gov.justice.laa.crime.dto.maat;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class JobSeekerDTO extends GenericDTO {
    private Boolean isJobSeeker;
    private Date lastSignedOn;

}