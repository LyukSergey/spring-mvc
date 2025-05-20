package com.lss.l102sringsecuritykeycloak.service;

import com.lss.l102sringsecuritykeycloak.resource.ProductResource;
import java.util.List;

public interface ProductService {

    List<ProductResource> getAll();

    ProductResource getById(Long id);

    ProductResource save(ProductResource product);

    void delete(Long id);

}
