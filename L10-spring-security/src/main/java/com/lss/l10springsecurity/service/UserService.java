package com.lss.l10springsecurity.service;

import com.lss.l10springsecurity.dto.UserDto;
import com.lss.l10springsecurity.mapper.UserMapper;
import com.lss.l10springsecurity.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('admin')")
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasAnyRole('admin', 'moderator')")
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new RuntimeException(String.format("User with id %s not found", id)));
    }
}
