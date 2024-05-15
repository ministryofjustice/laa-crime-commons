package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ContraryInterestDTO extends GenericDTO {
    public static final String NO_CONTRARY_INTEREST = "NOCON";
    private String code;
    private String description;
}
