package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum HardshipReviewDetailCode {
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
    OTHER("OTHER", "Other"),
    MEDICAL_GROUNDS("MEDICAL GROUNDS", "Medical Grounds"),
    SUSPENDED_WORK("SUSPENDED WORK", "Suspended from work"),
    OTHER_INC("OTHER INC", "Other");

    @JsonPropertyDescription("Hardship review detail codes that are valid")
    @JsonValue
    private final String code;
    private final String description;

    public static HardshipReviewDetailCode getFrom(String code) {
        if (StringUtils.isBlank(code)) { return null; }

        return Stream.of(HardshipReviewDetailCode.values())
                .filter(hrdCode -> hrdCode.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Hardship review detail code: %s does not exist.", code)));
    }

}
