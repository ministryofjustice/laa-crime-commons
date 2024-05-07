package uk.gov.justice.laa.crime.meansassessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import uk.gov.justice.laa.crime.enums.meansassessment.OutgoingType;

@Getter
@Builder
@AllArgsConstructor
public class Outgoing implements Amount {
    @Valid
    @NotNull
    @JsonProperty("outgoing_type")
    private OutgoingType outgoingType;

    @Valid
    @NotNull
    @JsonProperty("applicant")
    private FrequencyAmount applicant;

    @Valid
    @JsonProperty("partner")
    private FrequencyAmount partner;
}

