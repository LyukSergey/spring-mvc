package com.lss.l10springsecurityadmin.config;

import com.lss.l10springsecurityadmin.config.properties.SecurityProperty;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String ROLE_ATTRIBUTES = "role_attributes";
    private static final String ROLE = "ROLE_";
    private static final String PERMISSION = "PERMISSION_";
    private final SecurityProperty securityProperty;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(securityProperty.getCerts())
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            final List<String> roles = getRoles(jwt);
            final List<GrantedAuthority> authorities = createGrantedAuthorities(roles);
            final List<SimpleGrantedAuthority> permissions = Optional.ofNullable(jwt.getClaimAsMap(ROLE_ATTRIBUTES))
                    .map(Map::entrySet)
                    .stream()
                    .flatMap(Collection::stream)
                    .filter(entry -> entry.getValue() != null)
                    .filter(entry -> Boolean.parseBoolean(entry.getValue().toString()))
                    .map(entry -> new SimpleGrantedAuthority(PERMISSION.concat(entry.getKey())))
                    .toList();
            return Stream.concat(authorities.stream(), permissions.stream())
                    .collect(Collectors.toList());

        });
        return converter;
    }

    private static List<GrantedAuthority> createGrantedAuthorities(List<String> roles) {
        return roles.stream()
                .map(ROLE::concat)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private static List<String> getRoles(Jwt jwt) {
        return jwt.getClaimAsMap(REALM_ACCESS) != null
                ? Optional.ofNullable(jwt.getClaimAsMap(REALM_ACCESS))
                .map(stringObjectMap -> stringObjectMap.get(ROLES))
                .map(object -> (List<String>) object)
                .orElse(List.of())
                : Collections.emptyList();
    }
}
