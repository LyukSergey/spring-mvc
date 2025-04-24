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
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.printf("%s initialized%n", LoggingFilter.class.getSimpleName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.printf("Request received in %s, doFilter()%n", LoggingFilter.class.getSimpleName());
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = httpRequest.getRequestURI();
        List<String> skipExtensions = List.of(".map", ".js", ".css", ".ico", ".png", ".jpg");
        boolean skip = skipExtensions.stream().anyMatch(uri::endsWith);
        if (skip) {
            System.out.println("Skipping " + uri);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        BufferedResponseWrapper responseWrapper = new BufferedResponseWrapper(response);
        filterChain.doFilter(servletRequest, responseWrapper);

        String responseBody = responseWrapper.getCapturedAsString();
        System.out.printf("Response Body %s:%n %s %n", LoggingFilter.class.getSimpleName(), responseBody);

        ServletOutputStream out = response.getOutputStream();
        out.write(responseWrapper.getCapturedAsBytes());
        out.flush();
    }

    @Override
    public void destroy() {
        System.out.printf("%s destroy%n", LoggingFilter.class.getSimpleName());
    }
}
