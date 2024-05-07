package uk.gov.justice.laa.crime.enums.meansassessment;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IncomeType {
    EMPLOYMENT_INCOME("employment_income"),
    BENEFITS_FROM_WORK("benefits_from_work"),
    HOUSING_BENEFIT("housing_benefit"),
    CHILD_BENEFIT("child_benefit"),
    OTHER_INCOME("other_income"),
    INCOME_FROM_SAVINGS_OR_WITHDRAWALS("income_from_savings_or_withdrawals"),
    SELF_EMPLOYMENT_INCOME("self_employment_income"),
    WORKING_TAX_CREDITS("working_tax_credits"),
    INCAPACITY_BENEFIT("incapacity_benefit"),
    STATE_PENSION("state_pension"),
    INDUSTRIAL_INJURIES_DISABLEMENT_BENEFIT("industrial_injuries_disablement_benefit"),
    PRIVATE_PENSION("private_pension"),
    MAINTENANCE_INCOME("maintenance_income"),
    OTHER_BENEFITS("other_benefits");

    @NotNull
    @JsonValue
    private final String value;
}
