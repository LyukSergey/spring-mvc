package com.lss.l10springsecurityadmin.clients.impl;

import com.lss.l10springsecurityadmin.clients.HttpClientService;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HttpClientServiceImpl implements HttpClientService {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer %s";
    private final HttpClient httpClient;

    @Override
    public HttpResponse<String> get(String url, String token) {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(AUTHORIZATION, String.format(BEARER, token))
                .GET()
                .build();
        return makeRequest(request);
    }

    @Override
    public HttpResponse<String> post(String url, String body) {
        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return makeRequest(httpRequest);
    }

    private HttpResponse<String> makeRequest(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
