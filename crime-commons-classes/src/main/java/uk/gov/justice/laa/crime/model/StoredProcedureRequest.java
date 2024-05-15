package uk.gov.justice.laa.crime.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.justice.laa.crime.dto.maat.ApplicationDTO;
import uk.gov.justice.laa.crime.dto.maat.UserDTO;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StoredProcedureRequest {

    private String dbPackageName;
    private String procedureName;
    private ApplicationDTO application;
    private UserDTO user;
}
