package com.lss.l10springsecurityadmin.service.impl;

import com.lss.l10springsecurityadmin.service.DashboardService;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Override
    public String getDashboard() {
        return "Welcome to Admin Dashboard";
    }
}
