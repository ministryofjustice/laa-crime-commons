package uk.gov.justice.laa.crime.commons.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.tracing.BraveAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.tracing.zipkin.ZipkinAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import uk.gov.justice.laa.crime.commons.tracing.TraceIdHandler;
import zipkin2.reporter.AsyncReporter;

import static org.assertj.core.api.Assertions.assertThat;

class TracerAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TracingAutoConfiguration.class));

    @Test
    void traceIdHandlerIsConditionalOnTracerBean() {
        this.contextRunner
                .run((context) -> assertThat(context).doesNotHaveBean(TraceIdHandler.class));
    }

    @Test
    void traceConfigurerConfiguresTraceIdHandler() {
        this.contextRunner
                .withUserConfiguration(BraveAutoConfiguration.class)
                .run((context) -> assertThat(context).hasSingleBean(TraceIdHandler.class));
    }

    @Test
    void asyncReporterIsConditionalOnZipkinConfiguration() {
        this.contextRunner
                .run((context) -> assertThat(context).
                        doesNotHaveBean(AsyncReporter.class));
    }

    @Test
    void micrometerZipkinAutoConfigurerConfiguresAsyncReporter() {
        this.contextRunner
                .withUserConfiguration(ZipkinAutoConfiguration.class)
                .run((context) -> assertThat(context).
                        hasSingleBean(AsyncReporter.class));
    }

}
