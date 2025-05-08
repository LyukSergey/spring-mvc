package com.lss.l10springsecurityadmin.clients.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss.l10springsecurityadmin.clients.HttpClientService;
import com.lss.l10springsecurityadmin.clients.KeyClockClient;
import com.lss.l10springsecurityadmin.config.properties.SecurityProperty;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeyClockClientImpl implements KeyClockClient {

    private final HttpClientService httpClientService;
    private final SecurityProperty securityProperty;
    private final ObjectMapper objectMapper;

    @Override
    public String getClientName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return Optional.ofNullable(jwtAuth.getToken())
                    .map(jwt -> jwt.getClaim("azp"))
                    .filter(Objects::nonNull)
                    .map(object -> (String) object)
                    .orElseThrow(() -> new RuntimeException("Sub is not present in the token"));
        }
        throw new RuntimeException("Token is not JwtAuthenticationToken");
    }

    @Override
    public String getClientId(String clientName, String adminToken) {
        return Optional.ofNullable(
                        httpClientService.get(String.format(securityProperty.getClientIdUrl(), clientName), adminToken))
                .filter(stringHttpResponse -> stringHttpResponse.statusCode() == 200)
                .map(HttpResponse::body)
                .map(this::extractClientId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElseThrow(() -> new RuntimeException("Client id was not found"));
    }

    @Override
    public List<String> getGroups(String userId, String adminToken) {
        return Optional.ofNullable(
                        httpClientService.get(String.format(securityProperty.getUserGroupUrl(), userId), adminToken))
                .filter(stringHttpResponse -> stringHttpResponse.statusCode() == 200)
                .map(HttpResponse::body)
                .map(this::extractGroupIds)
                .stream()
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

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

    private List<String> extractGroupIds(String body) {
        final JsonNode rootNode = getJsonNode(body);
        final ArrayList<String> list = new ArrayList<>();
        for (JsonNode groupNode : rootNode) {
            groupNode.get("id").asText();
            Optional.ofNullable(groupNode.get("id"))
                    .map(JsonNode::asText)
                    .ifPresent(list::add);
        }
        return list;
    }

    private JsonNode getJsonNode(String body) {
        try {
            return objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can not parse json response", e);
        }
    }
}
