package com.lss.l10springsecurityadmin.service.impl;

import com.lss.l10springsecurityadmin.clients.HttpClientService;
import com.lss.l10springsecurityadmin.clients.KeyClockClient;
import com.lss.l10springsecurityadmin.security.SecurityService;
import com.lss.l10springsecurityadmin.config.properties.SecurityProperty;
import com.lss.l10springsecurityadmin.service.KeyClockService;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyClockServiceImpl implements KeyClockService {

    private final SecurityService securityService;
    private final KeyClockClient keyClockClient;
    private final HttpClientService httpClientService;
    private final SecurityProperty securityProperties;

    @Override
    public String getAllUsers() {
        final String tokenValue = securityService.getAdminAccessToken();
        return Optional.ofNullable(httpClientService.get(securityProperties.getUsersUrl(), tokenValue))
                .filter(stringHttpResponse -> stringHttpResponse.statusCode() == 200)
                .map(HttpResponse::body)
                .orElseThrow(() -> new RuntimeException("Key-clock users not found"));
    }


    @Override
    public String getUserRoles() {
        final String adminToken = securityService.getAdminAccessToken();
        final String userId = securityService.getUserId();
        final List<String> groupIds = keyClockClient.getGroups(userId, adminToken);

        return groupIds.stream()
                .map(groupId -> httpClientService.get(String.format(securityProperties.getUserGroupRolesUrl(), groupId),
                        adminToken))
                .filter(stringHttpResponse -> stringHttpResponse.statusCode() == 200)
                .map(HttpResponse::body)
                .collect(Collectors.joining(","));
    }
}
