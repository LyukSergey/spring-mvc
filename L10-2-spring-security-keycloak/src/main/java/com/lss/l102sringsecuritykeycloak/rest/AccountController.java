package com.lss.l102sringsecuritykeycloak.rest;

import com.lss.l102sringsecuritykeycloak.resource.AccountResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @GetMapping("/account")
    public AccountResource getAccount(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            throw new IllegalStateException("JWT token not found");
        }

        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");

        return new AccountResource(username, email);
    }

}
