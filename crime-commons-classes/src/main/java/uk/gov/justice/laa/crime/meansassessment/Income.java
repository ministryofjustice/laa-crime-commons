package uk.gov.justice.laa.crime.meansassessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import uk.gov.justice.laa.crime.enums.meansassessment.IncomeType;

@Getter
@Builder
@AllArgsConstructor
public class Income implements Amount {

    @JsonProperty("income_type")
    @Valid
    @NotNull
    private IncomeType incomeType;

    @Valid
    @NotNull
    @JsonProperty("applicant")
    private FrequencyAmount applicant;

    @Valid
    @JsonProperty("partner")
    private FrequencyAmount partner;
}

