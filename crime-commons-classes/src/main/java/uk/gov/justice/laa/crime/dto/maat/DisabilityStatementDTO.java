package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DisabilityStatementDTO extends GenericDTO {
    private String code;
    private String description;

    public static final String DECLARED_DISABLED = "DECLARED_DISABLED";
    public static final String NOT_DISABLED = "NOT_DISABLED";
}
