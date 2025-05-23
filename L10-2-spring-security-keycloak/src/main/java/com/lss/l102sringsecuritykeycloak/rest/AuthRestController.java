package com.lss.l102sringsecuritykeycloak.rest;

import com.lss.l102sringsecuritykeycloak.config.property.KeycloakProperty;
import com.lss.l102sringsecuritykeycloak.resource.LoginRequest;
import com.lss.l102sringsecuritykeycloak.resource.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    private final RestTemplate restTemplate;
    private final KeycloakProperty keycloakProperty;

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=password" +
                "&client_id=" + URLEncoder.encode(keycloakProperty.getAdminClientId(), StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(keycloakProperty.getAdminClientSecret(), StandardCharsets.UTF_8) +
                "&username=" + URLEncoder.encode(request.username(), StandardCharsets.UTF_8) +
                "&password=" + URLEncoder.encode(request.password(), StandardCharsets.UTF_8);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            final ResponseEntity<Object> responseEntity = restTemplate.exchange(
                    keycloakProperty.getAuthServerUrl() + "/realms/" + keycloakProperty.getRealm() + "/protocol/openid-connect/token",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            Map<String, Object> tokenResponse = Optional.ofNullable(responseEntity.getBody())
                    .map(o -> (Map<String, Object>) o)
                    .orElse(Map.of());
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // 1. Отримати admin access token
            HttpHeaders tokenHeaders = new HttpHeaders();
            tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String tokenBody = "grant_type=client_credentials" +
                    "&client_id=" + URLEncoder.encode(keycloakProperty.getAdminClientId(), StandardCharsets.UTF_8) +
                    "&client_secret=" + URLEncoder.encode(keycloakProperty.getAdminClientSecret(), StandardCharsets.UTF_8);
            HttpEntity<String> tokenRequest = new HttpEntity<>(tokenBody, tokenHeaders);

            Map<?, ?> tokenResponse = restTemplate.postForObject(
                    keycloakProperty.getAuthServerUrl() + "/realms/" + keycloakProperty.getRealm() + "/protocol/openid-connect/token",
                    tokenRequest,
                    Map.class
            );
            String accessToken = (String) tokenResponse.get("access_token");

            // 2. Створити користувача
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> userPayload = Map.of(
                    "username", request.username(),
                    "email", request.email(),
                    "enabled", true
            );

            HttpEntity<?> userEntity = new HttpEntity<>(userPayload, headers);

            ResponseEntity<Void> createUserResponse = restTemplate.exchange(
                    keycloakProperty.getAuthServerUrl() + "/admin/realms/" + keycloakProperty.getRealm() + "/users",
                    HttpMethod.POST,
                    userEntity,
                    Void.class
            );

            if (createUserResponse.getStatusCode().is2xxSuccessful()) {
                // 3. Знайти ID нового користувача
                ResponseEntity<List> usersResp = restTemplate.exchange(
                        keycloakProperty.getAuthServerUrl() + "/admin/realms/" + keycloakProperty.getRealm()
                                + "/users?username=" + request.username(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        List.class
                );

                if (usersResp.getBody() != null && !usersResp.getBody().isEmpty()) {
                    Map userObj = (Map) usersResp.getBody().get(0);
                    String userId = (String) userObj.get("id");

                    // 4. Встановити пароль
                    Map<String, Object> passwordPayload = Map.of(
                            "type", "password",
                            "value", request.password(),
                            "temporary", false
                    );
                    HttpEntity<?> passwordEntity = new HttpEntity<>(passwordPayload, headers);

                    restTemplate.put(
                            keycloakProperty.getAuthServerUrl() + "/admin/realms/" + keycloakProperty.getRealm()
                                    + "/users/" + userId + "/reset-password",
                            passwordEntity,
                            Void.class
                    );

                    // 5. Отримати роль "manager"
                    ResponseEntity<Map> roleResp = restTemplate.exchange(
                            keycloakProperty.getAuthServerUrl() + "/admin/realms/" + keycloakProperty.getRealm()
                                    + "/roles/MANAGER",
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            Map.class
                    );
                    Map<String, Object> managerRole = roleResp.getBody();

                    // 6. Призначити роль "manager" користувачу
                    HttpEntity<List<Map<String, Object>>> roleAssignEntity = new HttpEntity<>(
                            List.of(managerRole), headers
                    );

                    restTemplate.postForEntity(
                            keycloakProperty.getAuthServerUrl() + "/admin/realms/" + keycloakProperty.getRealm()
                                    + "/users/" + userId + "/role-mappings/realm",
                            roleAssignEntity,
                            Void.class
                    );

                    return ResponseEntity.ok(Map.of("message", "User registered with 'manager' role"));
                }
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "User creation failed"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }

    }

    @GetMapping("/auth/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String redirectUri = "http://localhost:8080/callback.html";

        String authUrl = UriComponentsBuilder
                .fromHttpUrl(
                        keycloakProperty.getAuthServerUrl() + "/realms/" + keycloakProperty.getRealm() + "/protocol/openid-connect/auth")
                .queryParam("client_id", keycloakProperty.getAdminClientId())
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile email")
                .queryParam("kc_idp_hint", "google")
                .build()
                .toUriString();

        response.sendRedirect(authUrl);
    }

    @PostMapping("/oauth/callback")
    public ResponseEntity<Map<String, Object>> handleCallback(@RequestBody Map<String, String> payload) {
        final String code = StringUtils.defaultIfBlank(payload.get("code"), "");
        // 1. Підготовка параметрів
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("client_id", keycloakProperty.getAdminClientId());
        params.add("redirect_uri", "http://localhost:8080/callback.html");
        params.add("client_secret", keycloakProperty.getAdminClientSecret());

        // 2. HTTP-запит у Keycloak
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        final ResponseEntity<Object> responseEntity = restTemplate.exchange(
                keycloakProperty.getAuthServerUrl() + "/realms/" + keycloakProperty.getRealm() + "/protocol/openid-connect/token",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        Map<String, Object> tokenResponse = Optional.ofNullable(responseEntity.getBody())
                .map(o -> (Map<String, Object>) o)
                .orElse(Map.of());
        return ResponseEntity.ok(tokenResponse);
    }


}
