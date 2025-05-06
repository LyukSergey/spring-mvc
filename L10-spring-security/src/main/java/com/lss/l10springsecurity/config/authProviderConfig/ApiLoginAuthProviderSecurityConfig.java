package com.lss.l10springsecurity.config.authProviderConfig;

import com.lss.l10springsecurity.security.authProvider.MyCustomAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApiLoginAuthProviderSecurityConfig {

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/local/login")  // Лише для /api/login
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
                .formLogin(AbstractHttpConfigurer::disable);   // опціонально
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, MyCustomAuthProvider customProvider) throws Exception {
        return new ProviderManager(customProvider);
    }
}
