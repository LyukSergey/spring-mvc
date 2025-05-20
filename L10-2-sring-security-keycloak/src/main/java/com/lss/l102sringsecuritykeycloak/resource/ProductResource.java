package com.lss.l102sringsecuritykeycloak.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResource {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String internalCode;
    private String imageUrl;
    private Boolean isAdmin;
}

