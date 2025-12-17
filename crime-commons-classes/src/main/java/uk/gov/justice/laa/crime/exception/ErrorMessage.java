package uk.gov.justice.laa.crime.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorMessage {
    private String field;
    private String message;
}
