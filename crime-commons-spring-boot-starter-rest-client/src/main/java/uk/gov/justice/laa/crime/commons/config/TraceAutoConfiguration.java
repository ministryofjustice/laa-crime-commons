package uk.gov.justice.laa.crime.commons.config;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.tracing.zipkin.ZipkinAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.justice.laa.crime.commons.tracing.TraceIdHandler;


/**
 * Autoconfigures <Code>TraceIdHandler</Code> bean for REST API Exception Handler and exclude <Code>ZipkinAutoConfiguration</Code>
 */
@Slf4j
@Configuration()
@AutoConfiguration
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = ZipkinAutoConfiguration.class)
public class TraceAutoConfiguration {

    /**
     * Configures a <code>TraceIdHandler</code> bean for REST API Exception Handler
     * @param tracer
     * @return TraceIdHandler
     */
    @Bean
    @ConditionalOnBean(Tracer.class)
    TraceIdHandler traceIdHandler(Tracer tracer) {
         return new TraceIdHandler(tracer);
    }

}
