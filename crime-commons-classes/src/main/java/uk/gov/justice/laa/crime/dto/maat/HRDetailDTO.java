package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HRDetailDTO extends GenericDTO {

    private Long id;
    private FrequenciesDTO frequency;
    private Date dateReceived;
    private HRDetailDescriptionDTO detailDescription;
    private String otherDescription;
    private BigDecimal amountNumber;
    private Date dateDue;
    private boolean accepted;
    private HRReasonDTO reason;
    private Timestamp timeStamp;
    private String hrReasonNote;

}
