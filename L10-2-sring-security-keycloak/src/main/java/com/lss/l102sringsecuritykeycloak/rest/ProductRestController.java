package com.lss.l102sringsecuritykeycloak.rest;

import com.lss.l102sringsecuritykeycloak.resource.ProductResource;
import com.lss.l102sringsecuritykeycloak.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<ProductResource> getProducts(HttpServletRequest request) {
        boolean isAdmin = Boolean.TRUE.equals(request.getAttribute("isAdmin"));
        return productService.getAll();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

}
