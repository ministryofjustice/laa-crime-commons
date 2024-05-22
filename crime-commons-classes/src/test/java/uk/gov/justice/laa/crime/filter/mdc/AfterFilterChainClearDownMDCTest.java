package uk.gov.justice.laa.crime.filter.mdc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
class AfterFilterChainClearDownMDCTest {

  private static final String MDC_KEY_FOR_TESTING = "mdcTestKey";
  private static final String MDC_VALUE_FOR_TESTING = "123456";

  @Mock
  private HttpServletRequest mockHttpRequest;

  @Mock
  private HttpServletResponse mockHttpResponse;

  @Mock
  private FilterChain mockFilterChain;

  private AfterFilterChainClearDownMDC filter;

  @BeforeEach
  void beforeEach() {
    MDC.clear();
    filter = new AfterFilterChainClearDownMDC();
    MDC.put(MDC_KEY_FOR_TESTING, MDC_VALUE_FOR_TESTING);
  }

  @AfterEach
  void afterEach() {
    MDC.clear();
  }

  @Test
  void should_ClearTheMappingDiagnosticsContext() throws ServletException, IOException {
    assertEquals(MDC_VALUE_FOR_TESTING, getActualValueFromMDC(), "Failed precondition");

    filter.doFilter(mockHttpRequest, mockHttpResponse, mockFilterChain);

    assertAll(
        () -> verify(mockFilterChain).doFilter(mockHttpRequest, mockHttpResponse),
        () -> assertNull(getActualValueFromMDC())
    );
  }

  @Test
  void should_ClearTheMappingDiagnosticsContextWhenAnExceptionIsThrown()
      throws ServletException, IOException {
    final ServletException expectedException = new ServletException("Error processing request");
    doThrow(expectedException)
        .when(mockFilterChain)
        .doFilter(mockHttpRequest, mockHttpResponse);
    assertEquals(MDC_VALUE_FOR_TESTING, getActualValueFromMDC(), "Failed precondition");

    ServletException actualException = assertThrows(ServletException.class,
        () -> filter.doFilter(mockHttpRequest, mockHttpResponse, mockFilterChain));

    assertAll(
        () -> assertEquals(expectedException, actualException),
        () -> verify(mockFilterChain).doFilter(mockHttpRequest, mockHttpResponse),
        () -> assertNull(getActualValueFromMDC())
    );
  }

  private String getActualValueFromMDC() {
    return MDC.get(MDC_KEY_FOR_TESTING);
  }
}