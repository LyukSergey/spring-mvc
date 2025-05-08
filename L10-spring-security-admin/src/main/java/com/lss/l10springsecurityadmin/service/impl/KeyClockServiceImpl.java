package com.lss.l10springsecurityadmin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss.l10springsecurityadmin.clients.HttpClientService;
import com.lss.l10springsecurityadmin.service.KeyClockSecurityService;
import com.lss.l10springsecurityadmin.config.properties.SecurityProperty;
import com.lss.l10springsecurityadmin.service.KeyClockService;
import java.net.http.HttpResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyClockServiceImpl implements KeyClockService {

    private final KeyClockSecurityService keyClockClient;
    private final HttpClientService httpClientService;
    private final SecurityProperty securityProperties;
    private final ObjectMapper objectMapper;

    @Override
    public String getAllUsers() {
        final String tokenValue = keyClockClient.getAdminAccessToken();
        return Optional.ofNullable(httpClientService.get(securityProperties.getUsersUrl(), tokenValue))
                .filter(stringHttpResponse -> stringHttpResponse.statusCode() == 200)
                .map(HttpResponse::body)
                .orElseThrow(() -> new RuntimeException("Key-clock users not found"));
    }


    @Override
    public String getUserRoles() {
        final String adminToken = keyClockClient.getAdminAccessToken();
        final String clientName = keyClockClient.getClientName();
        final String clientId = Optional.ofNullable(
                        httpClientService.get(String.format(securityProperties.getClientIdUrl(), clientName), adminToken))
                .filter(stringHttpResponse -> stringHttpResponse.statusCode() == 200)
                .map(HttpResponse::body)
                .map(this::extractClientId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElseThrow(() -> new RuntimeException("Client id was not found"));

//        httpClientService.get("http://localhost:8081/admin/realms/L10-spring-security-admin-realm/users/af7705ed-ac8d-4732-851f-e8823db982bf/role-mappings",
//                adminToken);
        return Optional.ofNullable(
                        httpClientService.get(String.format(securityProperties.getUserRolesUrl(), "af7705ed-ac8d-4732-851f-e8823db982bf", clientId),
                                adminToken))
                .filter(stringHttpResponse -> stringHttpResponse.statusCode() == 200)
                .map(HttpResponse::body)
                .orElseThrow(() -> new RuntimeException("Key-clock roles not found"));
    }

    private Optional<String> extractClientId(String body) {
        final JsonNode node = getJsonNode(body);
        if (node.isArray() && !node.isEmpty()) {
            return Optional.ofNullable(node.get(0))
                    .map(jsonNode -> jsonNode.get("id"))
                    .map(JsonNode::asText);
        }
        return Optional.empty();
    }

    private JsonNode getJsonNode(String body) {
        try {
            return objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can not parse json response", e);
        }
    }
}
