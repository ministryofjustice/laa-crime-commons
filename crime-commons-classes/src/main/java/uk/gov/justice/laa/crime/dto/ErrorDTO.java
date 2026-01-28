package uk.gov.justice.laa.crime.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ErrorDTO {
    String traceId;
    String code;
    String message;
    List<String> messageList;
}
