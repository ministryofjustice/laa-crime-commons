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
public class FdcItemDTO extends GenericDTO {

    private Long id;
    private String caseId;
    private String itemType;
    private String courtCode;
    private String supplierCode;
    private Boolean apportioned;
    private Boolean paidAsClaimed;
    private String latest;
    private Double cost;
    private Double vat;
    private Date costDate;

    private FdcAdjustmentReasonDTO adjustmentReason;

    public static final String LGFS_COST_TYPE = "LGFS";
    public static final String AGFS_COST_TYPE = "AGFS";

}
