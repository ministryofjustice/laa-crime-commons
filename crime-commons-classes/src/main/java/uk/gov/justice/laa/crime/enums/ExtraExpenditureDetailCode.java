package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ExtraExpenditureDetailCode {
    UNSECURED_LOAN("UNSECURED LOAN", "Unsecured Loan"),
    SECURED_LOAN("SECURED LOAN", "Secured Loan"),
    CAR_LOAN("CAR LOAN", "Car Loan"),
    IVA("IVA", "IVA"),
    CARDS("CARDS", "Credit/Store Card Payment"),
    DEBTS("DEBTS", "Debts"),
    FINES("FINES", "Fines"),
    RENT_ARREARS("RENT ARREARS", "Rent Arrears"),
    BAILIFF("BAILIFF", "Bailiff Costs"),
    DWP_OVERPAYMENT("DWP OVERPAYMENT", "DWP Overpayment"),
    STUDENT_LOAN("STUDENT LOAN", "Student Loan"),
    ADD_MORTGAGE("ADD MORTGAGE", "Mortgage on additional Property"),
    UNI_HOUSING("UNI HOUSING", "University Housing Costs"),
    PRESCRIPTION("PRESCRIPTION", "Prescription Costs"),
    PENSION_PAY("PENSION PAY", "Pension Payments"),
    MEDICAL_COSTS("MEDICAL COSTS", "Medical Costs"),
    OTHER("OTHER", "Other");

    @JsonPropertyDescription("Extra expenditure detail codes that are valid")
    private final String code;
    private final String description;

    public static ExtraExpenditureDetailCode getFrom(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        return Stream.of(ExtraExpenditureDetailCode.values())
                .filter(eedCode -> eedCode.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Extra expenditure detail code: %s does not exist.", code)));
    }
}
