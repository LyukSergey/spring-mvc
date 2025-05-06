package com.lss.l10springsecurityadmin.rest;

import com.lss.l10springsecurityadmin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public String users() {
        return userService.getUsers();
    }

}
