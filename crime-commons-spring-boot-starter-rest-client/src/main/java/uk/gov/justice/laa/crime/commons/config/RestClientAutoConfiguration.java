package uk.gov.justice.laa.crime.commons.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.resources.ConnectionProvider;
import uk.gov.justice.laa.crime.commons.client.RestAPIClient;
import uk.gov.justice.laa.crime.commons.common.Constants;
import uk.gov.justice.laa.crime.commons.filters.WebClientFilters;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;


/**
 * Autoconfigures <code>ConnectionProvider</code>, and several <Code>RestApiClient</Code> beans
 * for communicating with LAA crime APIs
 */
@Slf4j
@Configuration
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(RetryConfiguration.class)
public class RestClientAutoConfiguration {

    private final RetryConfiguration retryConfiguration;

    /**
     * Modifies request attributes to include the registrationId to be used to
     * look up the <code>OAuth2AuthorizedClient.</code>
     *
     * @param registrationId the registrationId
     * @return <code>Consumer</code> to populate the attributes
     * @see Consumer
     * @see ServletOAuth2AuthorizedClientExchangeFilterFunction
     */
    public static Consumer<Map<String, Object>> getExchangeFilterWith(String registrationId) {
        return ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId(registrationId);
    }

    /**
     * Configures a <code>WebClientCustomizer</code> with default headers and exchange filter functions
     * A <code>ServletOAuth2AuthorizedClientExchangeFilterFunction</code> filter is added to enable OAuth2
     * Requires at least one OAuth2 client to be configured, otherwise the required beans will not be instantiated
     * All <code>WebClient</code> beans built from the <code>WebClient.Builder</code> class will inherit from this customizer
     *
     * @return the web client customizer
     * @see WebClientFilters
     */
    @Bean
    @ConditionalOnBean(OAuth2AuthorizedClientRepository.class)
    WebClientCustomizer webClientCustomizer() {
        return webClientBuilder -> {

            ConnectionProvider provider =
                    ConnectionProvider.builder("custom")
                            .maxConnections(500)
                            .maxIdleTime(Duration.ofSeconds(20))
                            .maxLifeTime(Duration.ofSeconds(60))
                            .pendingAcquireTimeout(Duration.ofSeconds(60))
                            .evictInBackground(Duration.ofSeconds(120))
                            .build();

            webClientBuilder.clientConnector(new ReactorClientHttpConnector(
                    reactor.netty.http.client.HttpClient.create(provider)
                            .resolver(DefaultAddressResolverGroup.INSTANCE)
                            .compress(true)
                            .responseTimeout(Duration.ofSeconds(30))
            ));

            webClientBuilder.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            webClientBuilder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            webClientBuilder.codecs(configurer -> configurer
                    .defaultCodecs()
                    .maxInMemorySize(Constants.MAX_IN_MEMORY_SIZE)
            );

            webClientBuilder.filters(filters -> {
                filters.add(WebClientFilters.logResponse());
                filters.add(WebClientFilters.logRequestHeaders());
                filters.add(WebClientFilters.retryFilter(retryConfiguration));
//                filters.add(WebClientFilters.handleErrorResponse());
            });
        };
    }

    /**
     * Configures an <code>OAuth2AuthorizedClientManager</code> of type <code>AuthorizedClientServiceOAuth2AuthorizedClientManager</code> for use in non-servlet environments (e.g. SQS listeners, scheduled tasks)
     * Instantiation is conditional on the configuration of Spring Cloud access credentials
     *
     * @param clientService                the client service
     * @param clientRegistrationRepository the client registration repository
     * @return the OAuth 2 authorized client manager
     */
    @Bean
    @Order(2)
    @ConditionalOnProperty(name = "spring.cloud.aws.region.static")
    public OAuth2AuthorizedClientManager clientServiceAuthorizedClientManager(
            OAuth2AuthorizedClientService clientService, ClientRegistrationRepository clientRegistrationRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .refreshToken()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

    /**
     * Configures a WebClient bean with a <code>ServletOAuth2AuthorizedClientExchangeFilterFunction</code> via its builder.
     * Configuration is inherited from <code>WebClientCustomizer</code>. An OAuth2 filter is added here for authentication purposes
     *
     * @param builder the builder
     * @return the web client
     * @see WebClientCustomizer
     * @see ServletOAuth2AuthorizedClientExchangeFilterFunction
     */
    @Bean
    @Primary
    @ConditionalOnBean({WebClient.Builder.class, OAuth2AuthorizedClientRepository.class, ClientRegistrationRepository.class})
    WebClient defaultWebClient(WebClient.Builder builder, ClientRegistrationRepository clientRegistrations,
                               OAuth2AuthorizedClientRepository authorizedClients) {
        builder.filters(filters -> filters.add(
                0, new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                        clientRegistrations, authorizedClients
                )
        ));
        return builder.build();
    }

    /**
     * Configures a WebClient bean with a <code>ServletOAuth2AuthorizedClientExchangeFilterFunction</code> via its builder for use in non-servlet environments
     * (e.g. SQS listeners, scheduled tasks).
     * Configuration is inherited from <code>WebClientCustomizer</code>. Instantiation is conditional on the presence of a <code>OAuth2AuthorizedClientManager</code> bean with a specific qualifier
     *
     * @param builder                 the builder
     * @param authorizedClientManager the authorized client manager
     * @return the web client
     * @see OAuth2AuthorizedClientManager
     * @see AuthorizedClientServiceOAuth2AuthorizedClientManager
     */
    @Bean
    @ConditionalOnBean(value = {WebClient.Builder.class}, name = "clientServiceAuthorizedClientManager")
    WebClient nonServletWebClient(WebClient.Builder builder,
                                  @Qualifier("clientServiceAuthorizedClientManager")
                                  OAuth2AuthorizedClientManager authorizedClientManager) {
        builder.filters(filters ->
                filters.add(0, new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
                ));
        return builder.build();
    }

    /**
     * Configures a <code>RestApiClient</code> bean if an OAuth2 configuration for CDA is found
     *
     * @param webClient the web client
     * @return the rest api client
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.provider.cda.token-uri")
    RestAPIClient cdaApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "cda");
    }

    /**
     * Configures a <code>RestApiClient</code> bean if an OAuth2 configuration for MaatAPI is found
     *
     * @param webClient the web client
     * @return the rest api client
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.provider.maat-api.token-uri")
    RestAPIClient maatApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "maat-api");
    }

    /**
     * Configures a <code>RestApiClient</code> bean for communicating with MaatApi in non-servlet environments
     * (e.g. SQS listeners, scheduled tasks) if an OAuth2 configuration for MaatAPI is found and Spring Cloud
     * access credentials are configured
     *
     * @param webClient the web client
     * @return the rest api client
     */
    @Bean
    @ConditionalOnBean(name = "nonServletWebClient")
    @ConditionalOnProperty("spring.security.oauth2.client.provider.maat-api.token-uri")
    RestAPIClient maatApiNonServletClient(@Qualifier("nonServletWebClient") WebClient webClient) {
        return new RestAPIClient(webClient, "maat-api");
    }

    /**
     * Configures a <code>RestApiClient</code> bean for communicating with MaatApi in non-servlet environments
     * (e.g. SQS listeners, scheduled tasks) if an OAuth2 configuration for CDA API is found and Spring Cloud
     * access credentials are configured
     *
     * @param webClient
     * @return the rest api client
     */
    @Bean
    @ConditionalOnBean(name = "nonServletWebClient")
    @ConditionalOnProperty("spring.security.oauth2.client.provider.cda.token-uri")
    RestAPIClient cdaApiNonServletClient(@Qualifier("nonServletWebClient") WebClient webClient) {
        return new RestAPIClient(webClient, "cda");
    }

    /**
     * Configures a <code>RestApiClient</code> bean if an OAuth2 configuration for the Evidence service is found
     *
     * @param webClient the web client
     * @return the rest api client
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.provider.evidence.token-uri")
    RestAPIClient evidenceApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "evidence");
    }

    /**
     * Configures a <code>RestApiClient</code> bean if an OAuth2 configuration for the Hardship service is found
     *
     * @param webClient the web client
     * @return the rest api client
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.provider.hardship.token-uri")
    RestAPIClient hardshipApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "hardship");
    }

    /**
     * Configures a <code>RestApiClient</code> bean if an OAuth2 configuration for the CMA service is found
     *
     * @param webClient the web client
     * @return the rest api client
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.provider.cma.token-uri")
    RestAPIClient cmaApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "cma");
    }

    /**
     * Configures a <code>RestApiClient</code> bean if an OAuth2 configuration for the CCC service is found
     *
     * @param webClient the web client
     * @return the rest api client
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.provider.ccc.token-uri")
    RestAPIClient cccApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "ccc");
    }

    /**
     * Configures a <code>RestApiClient</code> bean if an OAuth2 configuration for the CCP service is found
     *
     * @param webClient the web client
     * @return the rest api client
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.provider.ccp.token-uri")
    RestAPIClient ccpApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "ccp");
    }

    /**
     * Configures a <code>RestApiClient</code> bean if an OAuth2 configuration for the Validation service is found
     *
     * @param webClient the web client
     * @return the rest api client
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.provider.validation.token-uri")
    RestAPIClient validationApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "validation");
    }

    /**
     * Configures a <code>RestApiClient</code> bean if an OAuth2 configuration for the Cat service is found
     *
     * @param webClient the web client
     * @return the rest api client
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.provider.cat.token-uri")
    RestAPIClient catApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "cat");
    }
}
