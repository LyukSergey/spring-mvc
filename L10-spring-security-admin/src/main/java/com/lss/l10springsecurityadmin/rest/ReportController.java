package com.lss.l10springsecurityadmin.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    @GetMapping("/reports")
    public String reports() {
        return "List of Reports";
    }
}
