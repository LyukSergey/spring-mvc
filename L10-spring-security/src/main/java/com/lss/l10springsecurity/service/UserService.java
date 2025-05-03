package com.lss.l10springsecurity.service;

import com.lss.l10springsecurity.entity.User;
import com.lss.l10springsecurity.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @PreAuthorize("hasRole('admin')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('admin', 'moderator')")
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
