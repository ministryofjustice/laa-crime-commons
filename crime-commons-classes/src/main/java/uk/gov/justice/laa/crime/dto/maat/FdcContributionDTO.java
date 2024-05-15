package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FdcContributionDTO extends GenericDTO {

    private Long id;
    private Long repId;
    private String status;
    private Boolean lgfsComplete;
    private Boolean agfsComplete;
    private Boolean manualAcceleration;
    private Collection<FdcItemDTO> lgfsCosts;
    private Collection<FdcItemDTO> agfsCosts;
    private DrcFileRefDTO drcFileRef;
    private Date dateCalculated;
    private Date dateCreated;
    private Date dateReplaced;
    private Double finalCost;
    private Double judicialApportionment;
    private Double lgfsCost;
    private Double agfsCost;
    private Double vat;
    private Collection<NoteDTO> notes;

}
