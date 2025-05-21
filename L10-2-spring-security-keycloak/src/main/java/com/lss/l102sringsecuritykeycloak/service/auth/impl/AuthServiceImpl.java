package com.lss.l102sringsecuritykeycloak.service.auth.impl;

import com.lss.l102sringsecuritykeycloak.config.property.KeycloakProperty;
import com.lss.l102sringsecuritykeycloak.service.auth.AuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RestTemplate restTemplate;
    private final KeycloakProperty keycloakProperty;

    public void registerUser(String username, String email, String password) {
        String token = getAdminAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> userPayload = Map.of(
                "username", username,
                "email", email,
                "enabled", true,
                "credentials", new Object[]{
                        Map.of(
                                "type", "password",
                                "value", password,
                                "temporary", false
                        )
                }
        );

        final HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);

        restTemplate.postForEntity(
                keycloakProperty.getAuthServerUrl() + "/admin/realms/" + keycloakProperty.getRealm() + "/users",
                request,
                String.class
        );
    }

    private String getAdminAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=client_credentials"
                + "&client_id="
                + keycloakProperty.getAdminClientId()
                + "&client_secret="
                + keycloakProperty.getAdminClientSecret();
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        Map<?, ?> response = restTemplate.postForObject(
                keycloakProperty.getAuthServerUrl() + "/realms/" + keycloakProperty.getRealm() + "/protocol/openid-connect/token",
                entity,
                Map.class
        );

        return response.get("access_token").toString();
    }
}
