package com.lss.l10springsecurity.rest;

import com.lss.l10springsecurity.dto.LoginDto;
import io.jsonwebtoken.Jwts;
import java.util.Collections;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/local")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final SecretKey jwtSecretKey;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String jwt = Jwts.builder()
                    .setSubject(authentication.getName())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 864_000_00))
                    .signWith(jwtSecretKey)
                    .compact();

            return ResponseEntity.ok(Collections.singletonMap("token", jwt));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }
}
