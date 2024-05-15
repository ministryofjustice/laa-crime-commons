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
public class PassportedDTO extends GenericDTO {
    private static final long serialVersionUID = 1L;

    private Long passportedId;
    private Long cmuId;
    private Long usn;

    private Date date;

    private AssessmentStatusDTO assessementStatusDTO;
    private PassportConfirmationDTO passportConfirmationDTO;
    private NewWorkReasonDTO newWorkReason;
    private ReviewTypeDTO reviewType;

    private String dwpResult;

    private Boolean benefitIncomeSupport;
    private JobSeekerDTO benefitJobSeeker;
    private Boolean benefitGaurenteedStatePension;
    private Boolean benefitClaimedByPartner;
    private Boolean benefitEmploymentSupport;
    private PartnerDTO partnerDetails;
    private String notes;
    private String result;
    private Boolean under18HeardYouthCourt;
    private Boolean under18HeardMagsCourt;
    private Boolean under18FullEducation;
    private Boolean under16;
    private Boolean between1617;

    private IncomeEvidenceSummaryDTO passportSummaryEvidenceDTO;

    private String whoDwpChecked;

}