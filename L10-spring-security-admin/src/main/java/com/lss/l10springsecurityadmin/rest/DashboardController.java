package com.lss.l10springsecurityadmin.rest;

import com.lss.l10springsecurityadmin.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return dashboardService.getDashboard();
    }
}
