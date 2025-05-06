package com.lss.l10springsecurityadmin.service.impl;

import com.lss.l10springsecurityadmin.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public String getUsers() {
        return "List of Users";
    }
}
