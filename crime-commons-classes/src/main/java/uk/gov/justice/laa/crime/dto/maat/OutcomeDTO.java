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
public class OutcomeDTO extends GenericDTO {
    private String outcome;
    private String description;
    private String outComeType;
    private Date dateSet;

    public static final String COMPLETE = "COMPLETE";

}
