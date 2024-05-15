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
public class PrintDateDTO extends GenericDTO {

    private static final long serialVersionUID = -8968823417266234052L;

    private Long id;
    private Long corrId;
    private Date printDate;

}
