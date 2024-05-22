package uk.gov.justice.laa.crime.filter.mdc;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@WebFilter("/*")
@Component
@Order
public class AfterFilterChainClearDownMDC implements Filter {
    
    @Override
    public void doFilter(ServletRequest servletRequest,
        ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            log.debug("About to clear down the MDC");
            MDC.clear();
        }
    }
}
