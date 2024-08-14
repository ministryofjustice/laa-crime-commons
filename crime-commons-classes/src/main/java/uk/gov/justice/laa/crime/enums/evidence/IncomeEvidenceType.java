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

    SIGNATURE("SIGNATURE", Constants.SIGNATURE, Constants.SIGNATURE, "Llofnod", false),
    CDS15("CDS15", Constants.CDS_15, Constants.CDS_15, Constants.CDS_15, true),
    FREEZING("FREEZING", Constants.FREEZING_ORDER, Constants.FREEZING_ORDER, "Gorchymyn Rhewi", true),
    RESTRAINING("RESTRAINING", Constants.RESTRAINT_ORDER, Constants.RESTRAINT_ORDER, "Gorchymyn Atal", true),
    CONFISCATION("CONFISCATION", Constants.CONFISCATION_ORDER, Constants.CONFISCATION_ORDER, "Gorchymyn Atafaeliad", true),
    OTHER_ADHOC("OTHER_ADHOC", Constants.OTHER_ADHOC, Constants.OTHER_ADHOC, "Ad Hoc eraill", true),
    EMP_LETTER_ADHOC("EMP LETTER ADHOC", Constants.LETTER_FROM_EMPLOYER, Constants.LETTER_FROM_EMPLOYER, "Llythyr o'r cyflogwr", true),
    WAGE_SLIP_ADHOC("WAGE SLIP ADHOC", "Wage Slip", "Wage Slip within past 3 months", "Papur Cyflog o fewn y tri mis diwethaf", true),
    NINO("NINO", Constants.NATIONAL_INSURANCE_NUMBER, Constants.NATIONAL_INSURANCE_NUMBER, "Rhif Yswiriant Cenedlaethol", false),
    ACCOUNTS("ACCOUNTS", Constants.SET_OF_ACCOUNTS, Constants.SET_OF_ACCOUNTS, "Cyfrifon", false),
    OTHER_BUSINESS("OTHER BUSINESS", Constants.OTHER_BUSINESS_RECORDS, Constants.OTHER_BUSINESS_RECORDS, "Cofnodion Busnes eraill", false),
    CASH_BOOK("CASH BOOK", Constants.CASH_BOOK, Constants.CASH_BOOK, "Llyfr Arian", false),
    WAGE_SLIP("WAGE SLIP", "Wage Slip", "Wage Slip within past 3 months", "Papur Cyflog o fewn y tri mis diwethaf", false),
    BANK_STATEMENT("BANK STATEMENT", "Bank Statement", "Bank Statement(s) covering 3 months", "Cyfriflen Banc", false),
    TAX_RETURN("TAX RETURN", Constants.TAX_RETURN, Constants.TAX_RETURN, "Ffurflen Dreth", false),
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
        public static final String OTHER_ADHOC = "Other Adhoc";
        public static final String NATIONAL_INSURANCE_NUMBER = "National Insurance Number";
        public static final String CASH_BOOK = "Cash Book";
        public static final String TAX_RETURN = "Tax Return";
        public static final String OTHER_BUSINESS_RECORDS = "Other Business Records";
        public static final String SET_OF_ACCOUNTS = "Set of Accounts";
        public static final String CONFISCATION_ORDER = "Confiscation order";
        public static final String RESTRAINT_ORDER = "Restraint Order";
        public static final String FREEZING_ORDER = "Freezing order";
        public static final String SIGNATURE = "Signature";
    }
}
