package uk.gov.justice.laa.crime.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Collection;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import uk.gov.justice.laa.crime.filter.mdc.AddLaaTransactionIdToMDC;
import uk.gov.justice.laa.crime.filter.mdc.AfterFilterChainClearDownMDC;

class FilterConfigurationTest {

  private FilterConfiguration filterConfiguration;

  @BeforeEach
  void setUp() {
    filterConfiguration = new FilterConfiguration();
  }

  @Test
  void addLaaTransactionIdToMDCFilter() {
    FilterRegistrationBean<AddLaaTransactionIdToMDC> addLaaTransactionId = filterConfiguration.addLaaTransactionIdToMDCFilter();

    Collection<String> actualUrlPatterns = addLaaTransactionId.getUrlPatterns();
    assertAll(
        () -> assertInstanceOf(AddLaaTransactionIdToMDC.class, addLaaTransactionId.getFilter()),
        () -> assertThat(actualUrlPatterns, Matchers.hasSize(1)),
        () -> assertThat(actualUrlPatterns, Matchers.contains("/*")),
        () -> assertEquals(Ordered.HIGHEST_PRECEDENCE, addLaaTransactionId.getOrder())
    );
  }

  @Test
  void afterFilterChainClearDownMDCFilter() {
    FilterRegistrationBean<AfterFilterChainClearDownMDC> clearDownMDC = filterConfiguration.afterFilterChainClearDownMDCFilter();

    Collection<String> actualUrlPatterns = clearDownMDC.getUrlPatterns();
    assertAll(
        () -> assertInstanceOf(AfterFilterChainClearDownMDC.class, clearDownMDC.getFilter()),
        () -> assertThat(actualUrlPatterns, Matchers.hasSize(1)),
        () -> assertThat(actualUrlPatterns, Matchers.contains("/*")),
        () -> assertEquals(Ordered.LOWEST_PRECEDENCE, clearDownMDC.getOrder())
    );
  }
}