package com.lss.l10springsecurity.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss.l10springsecurity.config.properties.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/keycloak")
@RequiredArgsConstructor
public class KeycloakLogoutController {

    private final JwtDecoder jwtDecoder;
    private final JwtProperties jwtProperties;  // assume has clientId and clientSecret

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "Missing or invalid Authorization header"));
            }

            String accessToken = authorizationHeader.substring(7);
            var jwt = jwtDecoder.decode(accessToken);
            String username = jwt.getSubject();
            System.out.println("✅ Logging out user: " + username);

            boolean revoked = logoutUserViaAdminApi(username);
            if (revoked) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Logout success"));
            } else {
                return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Failed to revoke token"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Exception: " + e.getMessage()));
        }
    }

    private boolean logoutUserViaAdminApi(String username) {
        try {
            if (username == null) {
                System.out.println("❌ User ID not found for username: " + username);
                return false;
            }
            String adminToken = getAdminAccessToken();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/admin/realms/L10-spring-security/users/" + username + "/logout"))
                    .header("Authorization", "Bearer " + adminToken)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                System.out.println("✅ User session terminated.");
                return true;
            } else {
                System.out.println("❌ Failed logout user. Status: " + response.statusCode());
                return false;
            }

        } catch (Exception e) {
            System.out.println("❌ Exception during admin logout: " + e.getMessage());
            return false;
        }
    }

    private String getAdminAccessToken() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        String body = "client_id=" + URLEncoder.encode(jwtProperties.getAdminClientId(), StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(jwtProperties.getAdminSecret(), StandardCharsets.UTF_8) +
                "&grant_type=client_credentials";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/realms/L10-spring-security/protocol/openid-connect/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            var json = new ObjectMapper().readTree(response.body());
            return json.get("access_token").asText();
        }

        throw new RuntimeException("Failed to get admin token: " + response.statusCode() + " - " + response.body());
    }
}