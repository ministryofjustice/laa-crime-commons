package uk.gov.justice.laa.crime.filter.mdc;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

@Slf4j
public class AddLaaTransactionIdToMDC implements Filter {

  private static final String LAA_TRANSACTION_ID = "Laa-Transaction-Id";

  @Override
  public void doFilter(ServletRequest request,
      ServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {

    String laaTransactionIdHeaderValue = extractLaaTransactionIdFromHeader(request);

    if (StringUtils.isNotBlank(laaTransactionIdHeaderValue)) {
      log.debug("Adding the {} [{}] to the MDC", LAA_TRANSACTION_ID, laaTransactionIdHeaderValue);
      MDC.put(LAA_TRANSACTION_ID, laaTransactionIdHeaderValue);

    } else {
      log.debug("Not adding the {} [{}] to the MDC, as it is blank", LAA_TRANSACTION_ID,
          laaTransactionIdHeaderValue);
    }

    chain.doFilter(request, response);
  }

  private String extractLaaTransactionIdFromHeader(ServletRequest request) {

    if (request instanceof HttpServletRequest httpServletRequest) {
      return httpServletRequest.getHeader(LAA_TRANSACTION_ID);
    }

    return null;
  }
}
