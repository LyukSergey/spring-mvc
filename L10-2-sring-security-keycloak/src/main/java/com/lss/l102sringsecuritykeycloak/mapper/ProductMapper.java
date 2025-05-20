package com.lss.l102sringsecuritykeycloak.mapper;

import com.lss.l102sringsecuritykeycloak.entity.Product;
import com.lss.l102sringsecuritykeycloak.resource.ProductResource;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResource toResource(Product entity);

    Product toEntity(ProductResource dto);

    List<ProductResource> toDtoList(List<Product> entities);
}

