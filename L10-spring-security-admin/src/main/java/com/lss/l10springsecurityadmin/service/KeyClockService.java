package com.lss.l10springsecurityadmin.service;

import org.springframework.security.access.prepost.PreAuthorize;

public interface KeyClockService {

    @PreAuthorize("hasRole('view_users')")
    String getAllUsers();

    @PreAuthorize("hasRole('view_roles')")
    String getUserRoles();

}
