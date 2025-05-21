package com.lss.l102sringsecuritykeycloak.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperty {

    private String authServerUrl;
    private String realm;
    private String adminClientId;
    private String adminClientSecret;

}
