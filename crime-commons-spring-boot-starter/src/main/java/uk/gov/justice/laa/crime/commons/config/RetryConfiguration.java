package uk.gov.justice.laa.crime.commons.config;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "retry-config")
public class RetryConfiguration {

    @NotNull
    private Integer maxRetries;

    @NotNull
    private Integer minBackOffPeriod;

    @NotNull
    private Double jitterValue;
}
