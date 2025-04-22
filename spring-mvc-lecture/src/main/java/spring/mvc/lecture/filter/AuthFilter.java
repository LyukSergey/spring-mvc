package spring.mvc.lecture.filter;

import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import spring.mvc.lecture.responseWrapper.BufferedResponseWrapper;

@Component
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.printf("%s initialized%n", AuthFilter.class.getSimpleName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.printf("Request received in %s, doFilter()%n", AuthFilter.class.getSimpleName());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.printf("%s destroy%n", AuthFilter.class.getSimpleName());
    }
}
