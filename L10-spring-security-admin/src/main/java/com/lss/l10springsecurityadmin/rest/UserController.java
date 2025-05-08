package com.lss.l10springsecurityadmin.rest;

import com.lss.l10springsecurityadmin.service.KeyClockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final KeyClockService keyClockService;

    @GetMapping("/users")
    public String users() {
        return keyClockService.getAllUsers();
    }

    @GetMapping("/roles")
    public String roles() {
        return keyClockService.getUserRoles();
    }

}
