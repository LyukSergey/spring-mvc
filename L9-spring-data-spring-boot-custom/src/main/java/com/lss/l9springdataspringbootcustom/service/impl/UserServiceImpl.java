package com.lss.l9springdataspringbootcustom.service.impl;

import com.lss.l9springDataJpaCustom.annotation.MyTransactional;
import com.lss.l9springdataspringbootcustom.converters.UserConverter;
import com.lss.l9springdataspringbootcustom.dto.UserDto;
import com.lss.l9springdataspringbootcustom.entity.Users;
import com.lss.l9springdataspringbootcustom.repository.UserRepository;
import com.lss.l9springdataspringbootcustom.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @MyTransactional
    public Users saveUser(UserDto user) {
        return userRepository.save(UserConverter.toEntity(user));
    }

    @MyTransactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @MyTransactional
    public List<UserDto> findAllUsers(boolean withPosts) {
        final List<Users> users = userRepository.findAll();
        return users.stream()
                .map(user -> UserConverter.toDto(user, withPosts))
                .toList();
    }

    public UserDto findUserById(Long id) {
        return Optional.ofNullable(userRepository.findById(id))
                .map(user -> UserConverter.toDto(user, false))
                .orElseThrow(() -> new RuntimeException(String.format("User with %s id not found", id)));
    }

    public List<UserDto> findUsersByName(String name, boolean withPosts) {
        return userRepository.findByName(name).stream()
                .map(user -> UserConverter.toDto(user, withPosts))
                .toList();
    }

}
