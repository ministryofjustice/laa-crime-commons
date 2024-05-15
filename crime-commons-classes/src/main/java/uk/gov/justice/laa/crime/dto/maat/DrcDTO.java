package uk.gov.justice.laa.crime.dto.maat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.justice.laa.crime.orchestration.dto.maat.DrcSupplierDTO;
import uk.gov.justice.laa.crime.orchestration.dto.maat.GenericDTO;

import java.util.Collection;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DrcDTO extends GenericDTO
{
	private DrcSupplierDTO drcSupplier;
	private Collection<DrcSupplierContactDTO> contacts;

}
