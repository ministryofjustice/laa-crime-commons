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
public class ContributionSummaryDTO extends GenericDTO {
    private Long id;
    private Double monthlyContribs;
    private Double upfrontContribs;
    private String basedOn;
    private Boolean upliftApplied;
    private Date effectiveDate;
    private Date calcDate;
    private String fileName;
    private Date dateFileSent;
    private Date dateFileReceived;

}
