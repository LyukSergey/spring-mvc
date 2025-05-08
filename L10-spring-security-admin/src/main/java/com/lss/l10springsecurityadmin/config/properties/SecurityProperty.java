package com.lss.l10springsecurityadmin.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProperty {

    private String realm;
    private String keyClockUrl;
    private String certs;
    private String tokenUrl;
    private String keyClockClientId;
    private String keyClockClientSecret;
    private String usersUrl;
    private String userRolesUrl;
    private String clientIdUrl;
}
