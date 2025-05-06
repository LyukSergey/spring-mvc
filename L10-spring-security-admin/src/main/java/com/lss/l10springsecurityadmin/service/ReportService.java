package com.lss.l10springsecurityadmin.service;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('report-viewer')")
public interface ReportService {

    @PreAuthorize("hasAuthority('PERMISSION_canViewReports')")
    String getReport();

}
