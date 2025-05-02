package com.lss.l9springdataspringbootcustom.service;

import com.lss.l9springdataspringbootcustom.dto.UserDto;
import com.lss.l9springdataspringbootcustom.entity.Users;
import java.util.List;

public interface UserService {

    Users saveUser(UserDto user);

    void deleteUser(Long id);

    List<UserDto> findAllUsers(boolean withPosts);

    UserDto findUserById(Long id);

    List<UserDto> findUsersByName(String name, boolean withPosts);
}
