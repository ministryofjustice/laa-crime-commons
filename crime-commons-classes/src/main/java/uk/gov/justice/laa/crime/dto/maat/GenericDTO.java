package uk.gov.justice.laa.crime.dto.maat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.justice.laa.crime.jackson.LocalDateTimeDeserializer;

@Data
@SuperBuilder
@NoArgsConstructor
public class GenericDTO {
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private String timestamp;
    private Boolean selected;
}
