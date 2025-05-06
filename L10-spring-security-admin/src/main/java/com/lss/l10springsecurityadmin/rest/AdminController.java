package com.lss.l10springsecurityadmin.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('admin')")
    public String dashboard() {
        return "Welcome to Admin Dashboard";
    }

    @GetMapping("/reports")
    @PreAuthorize("hasAuthority('PERMISSION_canViewReports')")
    public String reports() {
        return "List of Reports";
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('PERMISSION_canManageUsers')")
    public String users() {
        return "List of Users";
    }
}
