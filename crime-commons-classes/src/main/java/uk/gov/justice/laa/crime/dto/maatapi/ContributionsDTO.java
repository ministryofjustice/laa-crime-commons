package uk.gov.justice.laa.crime.dto.maatapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributionsDTO {
    private Integer id;
    private Integer applId;
    private Integer repId;
    private Integer contributionFileId;
    private LocalDate effectiveDate;
    private LocalDate calcDate;
    private BigDecimal contributionCap;
    private BigDecimal monthlyContributions;
    private BigDecimal upfrontContributions;
    private String upliftApplied;
    private String basedOn;
    private String transferStatus;
    private LocalDate dateUpliftApplied;
    private LocalDate dateUpliftRemoved;
    private LocalDateTime dateCreated;
    private String userCreated;
    private LocalDateTime dateModified;
    private String userModified;
    private String createContributionOrder;
    private Integer correspondenceId;
    private String active;
    private LocalDate replacedDate;
    private Boolean latest;
    private Integer ccOutcomeCount;
    private Integer seHistoryId;
}
