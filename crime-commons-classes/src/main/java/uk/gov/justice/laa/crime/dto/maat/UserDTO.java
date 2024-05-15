package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends GenericDTO {
    private String userName;
    private String firstName;
    private String initials;
    private String surname;
    private boolean enabled;
    private String password;
    private Date passwordExpiry;
    private Long lockedRepOrderId;
    private boolean loggedIn;
    private int loggingInAttempts;
    private AreaDTO areaDTO;
    private String userSession;
    private String sessionName;
    private String appName;
    private String appServer;
    private Collection<UserRoleDTO> userRoles;
    private CaseManagementUnitDTO selectedCMUDTO;
    private boolean locked;
}
