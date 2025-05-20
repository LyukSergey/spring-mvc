package com.lss.l102sringsecuritykeycloak.interceptor;

import com.lss.l102sringsecuritykeycloak.resource.ProductResource;
import com.lss.l102sringsecuritykeycloak.rest.ProductRestController;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Component
@ControllerAdvice
public class ProductResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getContainingClass() == ProductRestController.class &&
                returnType.getMethod() != null &&
                returnType.getMethod().getName().equals("getProducts");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {

        boolean isAdmin = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getAuthorities)
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN_USER"));

        if (body instanceof List<?> list) {
            return list.stream()
                    .map(item -> {
                        if (item instanceof ProductResource p) {
                            return new ProductResource(
                                    p.getId(), p.getName(), p.getDescription(),
                                    p.getPrice(), p.getInternalCode(), p.getImageUrl(), isAdmin
                            );
                        }
                        return item;
                    })
                    .toList();
        }

        return body;
    }
}