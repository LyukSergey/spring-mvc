package com.lss.l102sringsecuritykeycloak.resource;

public record ProductResource(
        Long id,
        String name,
        String description,
        Double price,
        String internalCode) {

}

