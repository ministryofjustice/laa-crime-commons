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
public class AreaDTO extends GenericDTO {

    private static final long serialVersionUID = 178738522671686929L;

    private Long areaId;
    private String code;
    private String description;
    private boolean enabled;
    private Date timeStamp;

    private Collection<CaseManagementUnitDTO> caseManagementUnits;

}
