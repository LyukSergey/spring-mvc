package com.lss.l10springsecurityadmin.service.impl;

import com.lss.l10springsecurityadmin.service.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public String getReport() {
        return "List of Reports";
    }
}
