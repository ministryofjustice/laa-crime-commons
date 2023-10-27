package uk.gov.justice.laa.crime.commons.config;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import uk.gov.justice.laa.crime.commons.tracing.TraceIdHandler;


/**
 * Autoconfigures <Code>TraceIdHandler</Code> bean for REST API Exception Handler and exclude <Code>ZipkinAutoConfiguration</Code>
 */
@Slf4j
@Configuration
@AutoConfiguration
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class TracingAutoConfiguration {

    /**
     * Configures a <code>TraceIdHandler</code> bean for REST API Exception Handler
     *
     * @param tracer
     * @return TraceIdHandler
     */
    @Bean
    @ConditionalOnBean(Tracer.class)
    TraceIdHandler traceIdHandler(Tracer tracer) {
        return new TraceIdHandler(tracer);
    }

}
