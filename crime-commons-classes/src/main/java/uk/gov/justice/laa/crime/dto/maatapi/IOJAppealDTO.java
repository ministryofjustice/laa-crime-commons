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
public class IOJAppealDTO {
    private Integer id;
    private Integer repId;
    private LocalDateTime appealSetupDate;
    private String nworCode;
    private LocalDateTime dateCreated;
    private String userCreated;
    private Integer cmuId;
    private String incomplete;
    private String appealSetupResult;
    private LocalDateTime decisionDate;
    private String decisionResult;
    private String iderCode;
    private String notes;
    private LocalDateTime dateModified;
    private String userModified;
    private String iapsStatus;
    private String replaced;
}
