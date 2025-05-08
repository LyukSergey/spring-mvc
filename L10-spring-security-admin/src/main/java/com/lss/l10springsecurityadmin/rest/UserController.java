package com.lss.l10springsecurityadmin.rest;

import com.lss.l10springsecurityadmin.service.KeyClockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final KeyClockService keyClockService;

    @GetMapping("/users")
    public ResponseEntity<String> users() {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(keyClockService.getAllUsers());
    }

    @GetMapping("/roles")
    public ResponseEntity<String> roles() {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(keyClockService.getUserRoles());
    }

}
