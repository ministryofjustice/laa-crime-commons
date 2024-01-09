package uk.gov.justice.laa.crime.commons.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.gov.justice.laa.crime.commons.tracing.TraceIdHandler;
import uk.gov.justice.laa.crime.commons.dto.ErrorDTO;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class DefaultExceptionHandler {

    private final TraceIdHandler traceIdHandler;

    private static ResponseEntity<ErrorDTO> buildErrorResponse(HttpStatus status, String message, String traceId) {
        return new ResponseEntity<>(
                ErrorDTO.builder().traceId(traceId).code(status.toString()).message(message).build(), status
        );
    }

    @ExceptionHandler(APIClientException.class)
    public ResponseEntity<ErrorDTO> handleApiClientException(APIClientException exception) {
        log.error("API Client Exception: {}", exception.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(),
                                  traceIdHandler.getTraceId()
        );
    }
}