package com.lss.l10springsecurityadmin.clients;

import java.util.List;

public interface KeyClockClient {

    String getClientName();

    String getClientId(String clientName, String adminToken);

    List<String> getGroups(String userId, String adminToken);
}
