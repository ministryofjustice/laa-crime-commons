package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CrownCourtOverviewDTO extends GenericDTO {
    private Boolean available;
    private CrownCourtSummaryDTO crownCourtSummaryDTO;
    private ContributionsDTO contribution;
    private Collection<ContributionSummaryDTO> contributionSummary;
    private ApplicantPaymentDetailsDTO applicantPaymentDetailsDTO;
    private Collection<CorrespondenceDTO> correspondence;
    private AppealDTO appealDTO;

}
