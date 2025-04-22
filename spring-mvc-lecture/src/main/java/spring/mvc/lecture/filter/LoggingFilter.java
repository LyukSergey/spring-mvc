package spring.mvc.lecture.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.stereotype.Component;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.printf("%s initialized%n", LoggingFilter.class.getSimpleName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.printf("Request received in %s, doFilter()%n", LoggingFilter.class.getSimpleName());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.printf("%s destroy%n", LoggingFilter.class.getSimpleName());
    }
}
