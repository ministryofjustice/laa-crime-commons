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
public class CaseManagementUnitDTO extends GenericDTO  {

    private static final long serialVersionUID = 2947962979790795526L;
    private Long cmuId;
    private Long areaId;
    private Boolean enabled;
    private String code;
    private Boolean libraAccess;
    private String name;
    private String description;
    private Date timeStamp;

}
