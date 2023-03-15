package uk.gov.justice.laa.crime.commons.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import uk.gov.justice.laa.crime.commons.client.RestAPIClient;
import uk.gov.justice.laa.crime.commons.filters.ExchangeFilterUtils;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;


@Slf4j
@Configuration()
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(RetryConfiguration.class)
public class WebClientAutoConfiguration {

    private final RetryConfiguration retryConfiguration;


    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("custom")
                .maxConnections(500)
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofSeconds(60))
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .evictInBackground(Duration.ofSeconds(120))
                .build();
    }

    @Bean
    @ConditionalOnBean({
            ClientRegistrationRepository.class,
            OAuth2AuthorizedClientRepository.class
    })
    public WebClientCustomizer webClientCustomizer(ConnectionProvider connectionProvider,
                                                   ClientRegistrationRepository clientRegistrations,
                                                   OAuth2AuthorizedClientRepository authorizedClients) {
        return webClientBuilder -> {

            webClientBuilder.clientConnector(new ReactorClientHttpConnector(
                            HttpClient.create(connectionProvider)
                                    .compress(true)
                                    .responseTimeout(Duration.ofSeconds(30))
                    )
            );

            webClientBuilder.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            webClientBuilder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            webClientBuilder.filters(filters -> {
                filters.add(ExchangeFilterUtils.logResponse());
                filters.add(ExchangeFilterUtils.logRequestHeaders());
                filters.add(ExchangeFilterUtils.retryFilter(retryConfiguration));
                filters.add(ExchangeFilterUtils.handleErrorResponse());

                filters.add(0, new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                                clientRegistrations, authorizedClients
                        )
                );

            });
        };
    }

    public static Consumer<Map<String, Object>> getExchangeFilterWith(String provider) {
        return ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId(provider);
    }

    @Bean
    @ConditionalOnBean(WebClient.Builder.class)
    public WebClient maatApiWebClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.registration.cda.token-uri")
    public RestAPIClient cdaApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "cda");
    }

    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.provider.maat-api.token-uri")
    public RestAPIClient maatApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "maat-api");
    }

}
