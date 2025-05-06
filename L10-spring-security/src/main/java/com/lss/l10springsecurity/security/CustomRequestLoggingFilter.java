package com.lss.l10springsecurity.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomRequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        System.out.printf("%s. Request to %s: %n", CustomRequestLoggingFilter.class.getSimpleName(), request.getRequestURI());
        long startTime = System.currentTimeMillis();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n--- Incoming Request ---\n");
        logMessage.append("Method: ").append(method).append("\n");
        logMessage.append("URI: ").append(uri);
        if (queryString != null) {
            logMessage.append("?").append(queryString);
        }
        logMessage.append("\n");
        logMessage.append("IP: ").append(clientIp).append("\n");
        logMessage.append("User-Agent: ").append(userAgent).append("\n");

        // Якщо є автентифікований користувач
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            logMessage.append("User: ").append(authentication.getName()).append("\n");

            if (authentication.getAuthorities() != null) {
                logMessage.append("Roles: ")
                        .append(authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(", ")))
                        .append("\n");
            }

            if (authentication.getCredentials() instanceof Jwt jwt) {
                logMessage.append("JWT Claims: ").append(jwt.getClaims()).append("\n");
            }
        } else {
            logMessage.append("User: Anonymous\n");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logMessage.append("Duration: ").append(duration).append(" ms\n");
            logMessage.append("--- End Request ---\n");

            System.out.println(logMessage);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Вимикаємо логування для static content, actuator чи swagger
        return path.startsWith("/swagger") || path.startsWith("/actuator") || path.startsWith("/webjars") || path.startsWith(
                "/favicon.ico");
    }
}
