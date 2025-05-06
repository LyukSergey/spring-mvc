package com.lss.l10springsecurityadmin.service;

import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

    @PreAuthorize("hasAuthority('PERMISSION_canManageUsers')")
    String getUsers();

}
