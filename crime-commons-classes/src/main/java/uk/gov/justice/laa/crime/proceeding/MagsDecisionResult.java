package uk.gov.justice.laa.crime.proceeding;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.justice.laa.crime.enums.DecisionReason;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MagsDecisionResult {
    @NotNull
    private LocalDate decisionDate;
    @NotNull
    private LocalDateTime timestamp;
    @NotNull
    private DecisionReason decisionReason;
}
