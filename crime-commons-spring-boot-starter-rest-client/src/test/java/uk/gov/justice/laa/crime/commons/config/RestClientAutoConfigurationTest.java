package uk.gov.justice.laa.crime.commons.config;

import org.eclipse.jetty.util.ArrayUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.tracing.BraveAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.assertj.AssertableWebApplicationContext;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import uk.gov.justice.laa.crime.commons.tracing.TraceIdHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class RestClientAutoConfigurationTest {

    private static final String CDA_REGISTRATION_ID = "cda";
    private static final String MAAT_API_REGISTRATION_ID = "maat-api";
    private static final String EVIDENCE_REGISTRATION_ID = "evidence";
    private static final String HARDSHIP_REGISTRATION_ID = "hardship";
    private static final String CCC_REGISTRATION_ID = "ccc";
    private static final String CCP_REGISTRATION_ID = "ccp";
    private static final String VALIDATION_REGISTRATION_ID = "validation";
    private static final String CDA_API_CLIENT_BEAN = "cdaApiClient";
    private static final String MAAT_API_CLIENT_BEAN = "maatApiClient";
    private static final String MAAT_API_NON_SERVLET_CLIENT_BEAN = "maatApiNonServletClient";
    private static final String EVIDENCE_API_CLIENT_BEAN = "evidenceApiClient";
    private static final String HARDSHIP_API_CLIENT_BEAN = "hardshipApiClient";
    private static final String CCC_API_CLIENT_BEAN = "cccApiClient";
    private static final String CCP_API_CLIENT_BEAN = "ccpApiClient";
    private static final String VALIDATION_API_CLIENT_BEAN = "validationApiClient";
    private static final String REGISTRATION_PREFIX = "spring.security.oauth2.client.registration";
    private static final String SPRING_CLOUD_PREFIX = "spring.cloud.aws";
    private static final String REGISTRATION_KEY_NAME =
            "org.springframework.security.oauth2.client.OAuth2AuthorizedClient.CLIENT_REGISTRATION_ID";
    private static final String OAUTH_CLIENT_PROVIDER_PREFIX = "spring.security.oauth2.client.provider";

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    RestClientAutoConfiguration.class, OAuth2ClientAutoConfiguration.class)
            );

    @Test
    void restClientConfigurerConfiguresWebClientCustomizer() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(CDA_REGISTRATION_ID))
                .run((context) -> {
                    assertThat(context).hasSingleBean(WebClientCustomizer.class);
                    List<ExchangeFilterFunction> filters = getFilters(context);
                    assertThat(filters).hasSize(4);
                    assertThat(getHeaders(context)).hasSize(2);
                    assertThat(getHeaders(context).getFirst(HttpHeaders.ACCEPT))
                            .isEqualTo(MediaType.APPLICATION_JSON_VALUE);
                    assertThat(getHeaders(context).getFirst(HttpHeaders.CONTENT_TYPE))
                            .isEqualTo(MediaType.APPLICATION_JSON_VALUE);
                });
    }

    @Test
    void webClientCustomizerBeanIsConditionalOnOAuth2AuthorizedClientRepository() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .run((context) -> assertThat(context).doesNotHaveBean(WebClientCustomizer.class));
    }

    @Test
    void restApiClientConfigurerConfiguresDefaultWebClientBean() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(CDA_REGISTRATION_ID))
                .run((context) -> {
                    assertThat(context).hasSingleBean(WebClient.class);
                    WebClient.Builder builder = (WebClient.Builder) ReflectionTestUtils.getField(
                            context.getBean(WebClient.class), "builder"
                    );
                    List<ExchangeFilterFunction> filters = getFilters(builder);
                    assertThat(filters.stream()
                            .anyMatch(exchangeFilterFunction ->
                                    exchangeFilterFunction instanceof ServletOAuth2AuthorizedClientExchangeFilterFunction
                            )
                    ).isTrue();
                    assertThat(filters).hasSize(5);
                });
    }

    @Test
    void restApiClientConfigurerConfiguresNonServletWebClientBean() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(ArrayUtil.add(
                                getSpringCloudPropertyValuesForClient(),
                                getOAuthPropertyValuesForClient(MAAT_API_CLIENT_BEAN)
                        )
                )
                .run((context) -> {
                    Map<String, WebClient> webClientBeans = context.getBeansOfType(WebClient.class);
                    assertThat(webClientBeans).hasSize(2);
                    WebClient.Builder builder = (WebClient.Builder) ReflectionTestUtils.getField(
                            webClientBeans.get("nonServletWebClient"), "builder"
                    );
                    List<ExchangeFilterFunction> filters = getFilters(builder);
                    assertThat(filters).hasSize(5);

                    ExchangeFilterFunction oAuthFilter = filters.stream()
                            .filter(exchangeFilterFunction ->
                                    exchangeFilterFunction instanceof ServletOAuth2AuthorizedClientExchangeFilterFunction
                            ).findFirst().orElseThrow();

                    OAuth2AuthorizedClientManager authorizedClientManager =
                            (OAuth2AuthorizedClientManager) ReflectionTestUtils.getField(
                                    oAuthFilter, "authorizedClientManager"
                            );
                    assertThat(authorizedClientManager)
                            .isInstanceOf(AuthorizedClientServiceOAuth2AuthorizedClientManager.class);
                });
    }

    @Test
    void cdaApiClientIsConditionalOnOAuthConfiguration() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(CDA_REGISTRATION_ID))
                .run((context) -> assertThat(context).doesNotHaveBean(CDA_API_CLIENT_BEAN));
    }

    @Test
    void restApiClientConfigurerConfiguresCdaApiClient() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(CDA_REGISTRATION_ID))
                .withPropertyValues(OAUTH_CLIENT_PROVIDER_PREFIX + ".cda.token-uri=mock-url")
                .run((context) -> assertThat(context).hasBean(CDA_API_CLIENT_BEAN));
    }

    @Test
    void maatApiClientIsConditionalOnOAuthConfiguration() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(MAAT_API_REGISTRATION_ID))
                .run((context) -> assertThat(context).doesNotHaveBean(MAAT_API_CLIENT_BEAN));
    }

    @Test
    void restApiClientConfigurerConfiguresMaatApiClient() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(MAAT_API_REGISTRATION_ID))
                .withPropertyValues(OAUTH_CLIENT_PROVIDER_PREFIX + ".maat-api.token-uri=mock-url")
                .run((context) -> assertThat(context).hasBean(MAAT_API_CLIENT_BEAN));
    }

    @Test
    void maatApiNonServletClientIsConditionalOnOAuthConfigurationAndNonServletWebClient() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(MAAT_API_REGISTRATION_ID))
                .run((context) -> assertThat(context).doesNotHaveBean(MAAT_API_NON_SERVLET_CLIENT_BEAN));
    }

    @Test
    void restApiClientConfigurerConfiguresMaatApiNonServletClient() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getSpringCloudPropertyValuesForClient())
                .withPropertyValues(getOAuthPropertyValuesForClient(MAAT_API_REGISTRATION_ID))
                .withPropertyValues(OAUTH_CLIENT_PROVIDER_PREFIX + ".maat-api.token-uri=mock-url")
                .run((context) -> assertThat(context).hasBean(MAAT_API_NON_SERVLET_CLIENT_BEAN));
    }

    @Test
    void evidenceApiClientIsConditionalOnOAuthConfiguration() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(EVIDENCE_REGISTRATION_ID))
                .run((context) -> assertThat(context).doesNotHaveBean(EVIDENCE_API_CLIENT_BEAN));
    }

    @Test
    void restApiClientConfigurerConfiguresEvidenceApiClient() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(EVIDENCE_REGISTRATION_ID))
                .withPropertyValues(OAUTH_CLIENT_PROVIDER_PREFIX + ".evidence.token-uri=mock-url")
                .run((context) -> assertThat(context).hasBean(EVIDENCE_API_CLIENT_BEAN));
    }

    @Test
    void hardshipApiClientIsConditionalOnOAuthConfiguration() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(HARDSHIP_REGISTRATION_ID))
                .run((context) -> assertThat(context).doesNotHaveBean(HARDSHIP_API_CLIENT_BEAN));
    }

    @Test
    void restApiClientConfigurerConfiguresHardshipApiClient() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(HARDSHIP_REGISTRATION_ID))
                .withPropertyValues(OAUTH_CLIENT_PROVIDER_PREFIX + ".hardship.token-uri=mock-url")
                .run((context) -> assertThat(context).hasBean(HARDSHIP_API_CLIENT_BEAN));
    }

    @Test
    void cccApiClientIsConditionalOnOAuthConfiguration() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(CCC_REGISTRATION_ID))
                .run((context) -> assertThat(context).doesNotHaveBean(CCC_API_CLIENT_BEAN));
    }

    @Test
    void restApiClientConfigurerConfiguresCccApiClient() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(CCC_REGISTRATION_ID))
                .withPropertyValues(OAUTH_CLIENT_PROVIDER_PREFIX + ".ccc.token-uri=mock-url")
                .run((context) -> assertThat(context).hasBean(CCC_API_CLIENT_BEAN));
    }

    @Test
    void ccpApiClientIsConditionalOnOAuthConfiguration() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(CCP_REGISTRATION_ID))
                .run((context) -> assertThat(context).doesNotHaveBean(CCP_API_CLIENT_BEAN));
    }

    @Test
    void restApiClientConfigurerConfiguresCcpApiClient() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(CCP_REGISTRATION_ID))
                .withPropertyValues(OAUTH_CLIENT_PROVIDER_PREFIX + ".ccp.token-uri=mock-url")
                .run((context) -> assertThat(context).hasBean(CCP_API_CLIENT_BEAN));
    }

    @Test
    void validationApiClientIsConditionalOnOAuthConfiguration() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(VALIDATION_REGISTRATION_ID))
                .run((context) -> assertThat(context).doesNotHaveBean(VALIDATION_API_CLIENT_BEAN));
    }

    @Test
    void restApiClientConfigurerConfiguresValidationApiClient() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(VALIDATION_REGISTRATION_ID))
                .withPropertyValues(OAUTH_CLIENT_PROVIDER_PREFIX + ".validation.token-uri=mock-url")
                .run((context) -> assertThat(context).hasBean(VALIDATION_API_CLIENT_BEAN));
    }

    @Test
    void givenRegistrationIdGetExchangeFilterWithPopulatesCorrectOAuthAttributes() {
        this.contextRunner
                .withUserConfiguration(TestConfig.class, WebClientAutoConfiguration.class)
                .withPropertyValues(getOAuthPropertyValuesForClient(MAAT_API_REGISTRATION_ID))
                .withPropertyValues(OAUTH_CLIENT_PROVIDER_PREFIX + ".maat-api.token-uri=mock-cda-url")
                .run((context) -> {
                    Map<String, Object> attributes = new HashMap<>(
                            Map.of(REGISTRATION_KEY_NAME, CDA_REGISTRATION_ID)
                    );
                    Consumer<Map<String, Object>> consumer =
                            RestClientAutoConfiguration.getExchangeFilterWith(MAAT_API_REGISTRATION_ID);
                    consumer.accept(attributes);
                    assertThat(attributes).hasSize(1);
                    assertThat(attributes).containsValue(MAAT_API_REGISTRATION_ID);
                });
    }

    private String[] getOAuthPropertyValuesForClient(String client) {
        return new String[]{
                String.format(REGISTRATION_PREFIX + ".%s.client-id=abc", client),
                String.format(REGISTRATION_PREFIX + ".%s.client-secret=secret", client),
                String.format(REGISTRATION_PREFIX + ".%s.provider=github", client)
        };
    }

    private String[] getSpringCloudPropertyValuesForClient() {
        return new String[]{
                SPRING_CLOUD_PREFIX + ".region.static=eu-west2"
        };
    }

    @SuppressWarnings("unchecked")
    private List<ExchangeFilterFunction> getFilters(AssertableWebApplicationContext context) {
        WebClient.Builder builder = context.getBean(WebClient.Builder.class);
        return (List<ExchangeFilterFunction>) ReflectionTestUtils.getField(builder, "filters");
    }

    @SuppressWarnings("unchecked")
    private List<ExchangeFilterFunction> getFilters(WebClient.Builder builder) {
        return (List<ExchangeFilterFunction>) ReflectionTestUtils.getField(builder, "filters");
    }

    private HttpHeaders getHeaders(AssertableWebApplicationContext context) {
        WebClient.Builder builder = context.getBean(WebClient.Builder.class);
        return (HttpHeaders) ReflectionTestUtils.getField(builder, "defaultHeaders");
    }

    @EnableWebSecurity
    @Configuration(proxyBeanMethods = false)
    static class TestConfig {

        @Bean
        TomcatServletWebServerFactory tomcat() {
            return new TomcatServletWebServerFactory(0);
        }

    }
}
