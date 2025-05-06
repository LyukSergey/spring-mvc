package com.lss.l10springsecurityadmin.service;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('admin')")
public interface DashboardService {

    String getDashboard();

}
