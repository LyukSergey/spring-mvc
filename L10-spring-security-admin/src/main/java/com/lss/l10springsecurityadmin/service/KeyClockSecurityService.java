package com.lss.l10springsecurityadmin.service;

public interface KeyClockSecurityService {

    String getAdminAccessToken();

    String getClientName();

    String getClientId(String clientName);

}
