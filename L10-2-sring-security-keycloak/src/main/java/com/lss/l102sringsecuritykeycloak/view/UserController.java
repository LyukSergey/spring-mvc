package com.lss.l102sringsecuritykeycloak.view;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/account")
    public String accountPage(Model model, @AuthenticationPrincipal Jwt jwt) {
        model.addAttribute("username", jwt.getClaimAsString("preferred_username"));
        model.addAttribute("email", jwt.getClaimAsString("email"));
        return "account";
    }
}
