package com.lss.l10springsecurity.config;

import com.lss.l10springsecurity.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("http://localhost:8081/realms/L10-spring-security/protocol/openid-connect/certs").build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain keycloakSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/keycloak/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.getWriter().write("Custom 403 (Keycloak)");
                        })
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.getWriter().write("Custom 401 (Keycloak)");
                        })
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                        .decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                ));
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain localJwtSecurityFilterChain(HttpSecurity http, SecretKey jwtSecretKey) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/local/**")
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/local/login").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.getWriter().write("Custom 403 (Local JWT)");
                        })
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.getWriter().write("Custom 401 (Local JWT)");
                        })
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtSecretKey), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/local/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            System.out.println("Local user logged out: " + (authentication != null ? authentication.getName() : "anonymous"));
                        })
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("Local logout success!");
                        })
                );
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsMap("realm_access") != null
                    ? (List<String>) jwt.getClaimAsMap("realm_access").get("roles")
                    : Collections.emptyList();

            // список системних ролей, які ми ігноруємо
            Set<String> excludedRoles = Set.of(
                    "offline_access",
                    "uma_authorization",
                    "default-roles-l10-spring-security"  // заміни на свій default-roles-* якщо інша назва
            );

            return roles.stream()
                    .filter(role -> !excludedRoles.contains(role))  // прибираємо тільки системні
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        });
        return jwtAuthenticationConverter;
    }

}
