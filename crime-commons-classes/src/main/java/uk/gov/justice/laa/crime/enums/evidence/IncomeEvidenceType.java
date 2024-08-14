package uk.gov.justice.laa.crime.enums.evidence;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum IncomeEvidenceType {

    SIGNATURE("SIGNATURE", "Signature", "Signature", "Llofnod", false),
    CDS15("CDS15", Constants.CDS_15, Constants.CDS_15, Constants.CDS_15, true),
    FREEZING("FREEZING", "Freezing order", "Freezing order", "Gorchymyn Rhewi", true),
    RESTRAINING("RESTRAINING", "Restraint Order", "Restraint Order", "Gorchymyn Atal", true),
    CONFISCATION("CONFISCATION", "Confiscation order", "Confiscation order", "Gorchymyn Atafaeliad", true),
    OTHER_ADHOC("OTHER_ADHOC", "Other Adhoc", "Other Adhoc", "Ad Hoc eraill", true),
    EMP_LETTER_ADHOC("EMP LETTER ADHOC", Constants.LETTER_FROM_EMPLOYER, Constants.LETTER_FROM_EMPLOYER, "Llythyr o'r cyflogwr", true),
    WAGE_SLIP_ADHOC("WAGE SLIP ADHOC", "Wage Slip", "Wage Slip within past 3 months", "Papur Cyflog o fewn y tri mis diwethaf", true),
    NINO("NINO", "National Insurance Number", "National Insurance Number", "Rhif Yswiriant Cenedlaethol", false),
    ACCOUNTS("ACCOUNTS", "Set of Accounts", "Set of Accounts", "Cyfrifon", false),
    OTHER_BUSINESS("OTHER BUSINESS", "Other Business Records", "Other Business Records", "Cofnodion Busnes eraill", false),
    CASH_BOOK("CASH BOOK", "Cash Book", "Cash Book", "Llyfr Arian", false),
    WAGE_SLIP("WAGE SLIP", "Wage Slip", "Wage Slip within past 3 months", "Papur Cyflog o fewn y tri mis diwethaf", false),
    BANK_STATEMENT("BANK STATEMENT", "Bank Statement", "Bank Statement(s) covering 3 months", "Cyfriflen Banc", false),
    TAX_RETURN("TAX RETURN", "Tax Return", "Tax Return", "Ffurflen Dreth", false),
    EMP_LETTER("EMP LETTER", Constants.LETTER_FROM_EMPLOYER, Constants.LETTER_FROM_EMPLOYER, "Llythyr oddi wrth Gyflogwr", false),
    OTHER("OTHER", "Other Ad-hoc evidence", "Text to be entered", "", false);

    private final String name;
    private final String description;
    private final String letterDescription;
    private final String welshLetterDescription;
    private final boolean isExtra;

    public static IncomeEvidenceType getFrom(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }

        return Stream.of(IncomeEvidenceType.values())
                .filter(type -> type.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Income Evidence Type with value: %s does not exist.", name)));
    }

    private static class Constants {
        public static final String CDS_15 = "CDS 15";
        public static final String LETTER_FROM_EMPLOYER = "Letter from Employer";
    }
}
