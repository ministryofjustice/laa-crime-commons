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
public class NoteDTO extends GenericDTO {

    private static final long serialVersionUID = -6856254823228346707L;

    private Long noteId;
    private Long fdcId;
    private UserDTO author;
    private String userCreated;
    private Date dateCreated;
    private String note;

}
