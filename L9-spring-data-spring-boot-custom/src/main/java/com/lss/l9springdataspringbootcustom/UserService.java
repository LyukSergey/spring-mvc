package com.lss.l9springdataspringbootcustom;

import java.util.List;

public interface UserService {

    Users saveUser(UserDto user);

    void deleteUser(Long id);

    List<UserDto> findAllUsers();

    UserDto findUserById(Long id);

    List<UserDto> findUsersByName(String name);
}
