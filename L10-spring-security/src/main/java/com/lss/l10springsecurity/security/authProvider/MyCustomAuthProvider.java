package com.lss.l10springsecurity.security.authProvider;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyCustomAuthProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService; // або своя логіка

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = getPassword(authentication);

        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user != null && password.equals(user.getPassword())) { // своя перевірка
            return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    private String getPassword(Authentication authentication) {
        return Optional.ofNullable(authentication.getCredentials())
                .map(Object::toString)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
