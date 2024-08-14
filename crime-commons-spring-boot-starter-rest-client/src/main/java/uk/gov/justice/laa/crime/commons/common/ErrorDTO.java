package uk.gov.justice.laa.crime.commons.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class ErrorDTO extends Throwable {
    String traceId;
    String code;
    String message;
}
