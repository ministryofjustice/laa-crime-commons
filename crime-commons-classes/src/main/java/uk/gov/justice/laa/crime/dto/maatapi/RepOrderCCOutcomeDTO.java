package uk.gov.justice.laa.crime.dto.maatapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepOrderCCOutcomeDTO {

    private int id;
    private int repId;
    private String outcome;
    private LocalDateTime outcomeDate;
    private String userCreated;
    private LocalDateTime dateCreated;
    private String caseNumber;
    private String crownCourtCode;
    private String userModified;
    private LocalDateTime dateModified;
}
