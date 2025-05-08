package com.lss.l10springsecurityadmin.security.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss.l10springsecurityadmin.clients.HttpClientService;
import com.lss.l10springsecurityadmin.security.SecurityService;
import com.lss.l10springsecurityadmin.config.properties.SecurityProperty;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    public static final String ACCESS_TOKEN = "access_token";

    private final SecurityProperty securityProperties;
    private final ObjectMapper objectMapper;
    private final HttpClientService httpClientService;

    @Override
    public String getAdminAccessToken() {
        final String body = getBody();
        httpClientService.post(securityProperties.getTokenUrl(), body);
        final HttpResponse<String> response = httpClientService.post(securityProperties.getTokenUrl(), body);
        return getToken(response);
    }

    @Override
    public String getUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(object -> (Jwt) object)
                .map(JwtClaimAccessor::getSubject)
                .orElseThrow(() -> new RuntimeException("User id was not found"));
    }

    private String getToken(HttpResponse<String> response) {
        if (response.statusCode() == 200) {
            final JsonNode jsonNode = getJsonNode(response.body());
            return jsonNode.get(ACCESS_TOKEN).asText();
        }
        throw new RuntimeException(
                String.format("Failed to get admin token. Response code: %s. Error message: %s", response.statusCode(), response.body()));
    }

    private JsonNode getJsonNode(String body) {
        try {
            return objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error read key-clock tree", e);
        }
    }

    private String getBody() {
        return String.format(
                "grant_type=client_credentials&client_id=%s&client_secret=%s",
                URLEncoder.encode(securityProperties.getKeyClockClientId(), StandardCharsets.UTF_8),
                URLEncoder.encode(securityProperties.getKeyClockClientSecret(), StandardCharsets.UTF_8)
        );
    }
}
