package com.lss.l10springsecurityadmin.clients;

import java.net.http.HttpResponse;

public interface HttpClientService {

    HttpResponse<String> get(String url, String token);

    HttpResponse<String> post(String url, String body);

}
