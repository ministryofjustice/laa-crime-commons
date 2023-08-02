package uk.gov.justice.laa.crime.commons.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.HttpClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import uk.gov.justice.laa.crime.commons.client.RestAPIClient;
import uk.gov.justice.laa.crime.commons.common.Constants;
import uk.gov.justice.laa.crime.commons.filters.WebClientFilters;

import java.util.Map;
import java.util.function.Consumer;


/**
 * Autoconfigures <code>ConnectionProvider</code>, and several <Code>RestApiClient</Code> beans
 * for communicating with LAA crime APIs
 */
@Slf4j
@Configuration()
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(RetryConfiguration.class)
public class RestClientAutoConfiguration {

    private final RetryConfiguration retryConfiguration;


    /**
     * Configures a <code>WebClientCustomizer</code> with default headers and exchange filter functions
     * A <code>ServletOAuth2AuthorizedClientExchangeFilterFunction</code> filter is added to enable OAuth2
     * Requires at least one OAuth2 client to be configured, otherwise the required beans will not be instantiated
     * All <code>WebClient</code> beans built from the <code>WebClient.Builder</code> class will inherit from this customizer
     *
     * @param clientRegistrations the client registration repository
     * @param authorizedClients   the authorized client repository
     * @return the web client customizer
     * @see WebClientFilters
     */
    @Bean
    @ConditionalOnBean(OAuth2AuthorizedClientRepository.class)
    WebClientCustomizer webClientCustomizer(ClientRegistrationRepository clientRegistrations,
                                            OAuth2AuthorizedClientRepository authorizedClients) {
        return webClientBuilder -> {


            HttpClient client = new HttpClient();
            webClientBuilder.clientConnector(new JettyClientHttpConnector(client));

            webClientBuilder.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            webClientBuilder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            webClientBuilder.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(Constants.MAX_IN_MEMORY_SIZE));

            webClientBuilder.filters(filters -> {
                filters.add(WebClientFilters.logResponse());
                filters.add(WebClientFilters.logRequestHeaders());
                filters.add(WebClientFilters.retryFilter(retryConfiguration));
                filters.add(WebClientFilters.handleErrorResponse());

                filters.add(0, new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                                clientRegistrations, authorizedClients
                        )
                );

            });
        };
    }

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
     * Configures a WebClient bean via a builder instance
     * Configuration is inherited from <code>WebClientCustomizer</code>
     *
     * @param builder the builder
     * @return the web client
     * @see WebClientCustomizer
     */
    @Bean
    @ConditionalOnBean(WebClient.Builder.class)
    WebClient maatApiWebClient(WebClient.Builder builder) {
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

    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.client.provider.evidence.token-uri")
    RestAPIClient evidenceApiClient(WebClient webClient) {
        return new RestAPIClient(webClient, "evidence");
    }

}
