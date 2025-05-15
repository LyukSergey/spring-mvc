package com.lss.l101springsecuritydb.service;

import com.lss.l101springsecuritydb.dto.RegisterRequest;
import com.lss.l101springsecuritydb.dto.UserResource;
import java.util.List;

public interface UserService {

    List<UserResource> getAllUsers();

    UserResource getUserByUsername(String username);

    boolean existsByUsername(String username);

    void registerNewUser(RegisterRequest request);
}
