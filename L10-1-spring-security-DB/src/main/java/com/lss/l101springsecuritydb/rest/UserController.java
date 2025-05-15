package com.lss.l101springsecuritydb.rest;

import com.lss.l101springsecuritydb.dto.UserResource;
import com.lss.l101springsecuritydb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResource getCurrentUser(Principal principal) {
        return userService.getUserByUsername(principal.getName());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResource> getAllUsers() {
        return userService.getAllUsers();
    }
}
