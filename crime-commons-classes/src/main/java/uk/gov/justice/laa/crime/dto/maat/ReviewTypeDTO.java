/**
 *
 */
package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReviewTypeDTO extends GenericDTO {

    private static final long serialVersionUID = 5645077250308954629L;

    private String code;
    private String description;

}
