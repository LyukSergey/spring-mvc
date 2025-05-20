package com.lss.l102sringsecuritykeycloak.service.auth;

public interface AuthService {

    void registerUser(String username, String email, String password);
}
