package com.lss.l102sringsecuritykeycloak.rest;

import com.lss.l102sringsecuritykeycloak.resource.ProductResource;
import com.lss.l102sringsecuritykeycloak.service.ProductService;
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
    public List<ProductResource> getProducts() {
        return productService.getAll();
    }

    @GetMapping("/products/{id}")
    public ProductResource getProduct(@PathVariable Long id) {
        return productService.getById(id);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

}
