package com.lss.l102sringsecuritykeycloak.view;

import com.lss.l102sringsecuritykeycloak.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public String home(Model model, @AuthenticationPrincipal Jwt jwt) {
        model.addAttribute("products", productService.getAll());
        model.addAttribute("loggedIn", jwt != null);
        return "home";
    }
}
