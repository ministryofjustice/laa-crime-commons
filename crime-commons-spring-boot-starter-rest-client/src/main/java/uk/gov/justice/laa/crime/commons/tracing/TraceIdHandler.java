package uk.gov.justice.laa.crime.commons.tracing;

import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * TraceIdHandler is designed to get the current traceId for REST API Exception Handlers.
 * In the case of any service level errors or exceptions, TraceIdHandler can be used to add the
 * current traceId to Error response which helps to track down errors easily.
 *
 */
@RequiredArgsConstructor
@Component
public class TraceIdHandler {

    private final Tracer tracer;

    public String getTraceId() {
        return Optional.of(tracer).
                map(Tracer::currentTraceContext).
                map(CurrentTraceContext::context).
                map(TraceContext::traceId).
                orElse("");
    }
}
