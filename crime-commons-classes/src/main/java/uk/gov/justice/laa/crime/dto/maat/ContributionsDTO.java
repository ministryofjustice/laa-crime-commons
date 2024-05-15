package uk.gov.justice.laa.crime.dto.maat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.justice.laa.crime.orchestration.jackson.SysGenDateDeserializer;

import java.math.BigDecimal;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ContributionsDTO extends GenericDTO {
    private Long id;
    private BigDecimal monthlyContribs;
    private BigDecimal upfrontContribs;
    @JsonDeserialize(using = SysGenDateDeserializer.class)
    private SysGenDate effectiveDate;
    @JsonDeserialize(using = SysGenDateDeserializer.class)
    private SysGenDate calcDate;
    private BigDecimal capped;
    private boolean upliftApplied;
    private SysGenString basedOn;

}
