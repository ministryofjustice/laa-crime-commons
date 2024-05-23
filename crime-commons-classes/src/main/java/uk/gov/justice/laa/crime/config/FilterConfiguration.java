package uk.gov.justice.laa.crime.config;

import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import uk.gov.justice.laa.crime.filter.mdc.AddLaaTransactionIdToMDC;
import uk.gov.justice.laa.crime.filter.mdc.AfterFilterChainClearDownMDC;

/**
 * Autoconfigures the filters <Code>AddLaaTransactionIdToMDC</Code> and
 * <Code>AfterFilterChainClearDownMDC</Code>
 */
@Configuration
@AutoConfiguration
public class FilterConfiguration {

  private static final String MATCH_ALL_URLS = "/*";

  @Bean
  @ConditionalOnClass(MDC.class)
  public FilterRegistrationBean<AddLaaTransactionIdToMDC> addLaaTransactionIdToMDCFilter() {
    FilterRegistrationBean<AddLaaTransactionIdToMDC> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new AddLaaTransactionIdToMDC());
    registrationBean.addUrlPatterns(MATCH_ALL_URLS);
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);

    return registrationBean;
  }

  @Bean
  @ConditionalOnClass(MDC.class)
  public FilterRegistrationBean<AfterFilterChainClearDownMDC> afterFilterChainClearDownMDCFilter() {
    FilterRegistrationBean<AfterFilterChainClearDownMDC> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new AfterFilterChainClearDownMDC());
    registrationBean.addUrlPatterns(MATCH_ALL_URLS);
    registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);

    return registrationBean;
  }
}
