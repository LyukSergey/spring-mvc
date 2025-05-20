package com.lss.l102sringsecuritykeycloak.service.impl;

import com.lss.l102sringsecuritykeycloak.entity.Product;
import com.lss.l102sringsecuritykeycloak.mapper.ProductMapper;
import com.lss.l102sringsecuritykeycloak.repository.ProductRepository;
import com.lss.l102sringsecuritykeycloak.resource.ProductResource;
import com.lss.l102sringsecuritykeycloak.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN_USER', 'MANAGER')")
    public List<ProductResource> getAll() {
        return productRepository.findAll().stream()
                .map(mapper::toResource)
                .toList();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN_USER', 'MANAGER')")
    public ProductResource getById(Long id) {
        return productRepository.findById(id)
                .map(mapper::toResource)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN_USER')")
    public ProductResource save(ProductResource product) {
        final Product entity = mapper.toEntity(product);
        return mapper.toResource(productRepository.save(entity));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN_USER')")
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
