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
public class AppealDTO extends GenericDTO {
    private Boolean available;
    private Date appealReceivedDate;
    private Date appealSentenceOrderDate;
    private Date appealSentOrderDateSet;
    private AppealTypeDTO appealTypeDTO;
}
