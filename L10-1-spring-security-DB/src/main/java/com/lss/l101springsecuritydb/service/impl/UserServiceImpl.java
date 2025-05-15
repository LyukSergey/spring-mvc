package com.lss.l101springsecuritydb.service.impl;

import com.lss.l101springsecuritydb.dto.RegisterRequest;
import com.lss.l101springsecuritydb.dto.UserResource;
import com.lss.l101springsecuritydb.entity.ConfirmationToken;
import com.lss.l101springsecuritydb.entity.Role;
import com.lss.l101springsecuritydb.entity.User;
import com.lss.l101springsecuritydb.mapper.UserMapper;
import com.lss.l101springsecuritydb.repository.ConfirmationTokenRepository;
import com.lss.l101springsecuritydb.repository.RoleRepository;
import com.lss.l101springsecuritydb.repository.UserRepository;
import com.lss.l101springsecuritydb.service.EmailService;
import com.lss.l101springsecuritydb.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    @Override
    public List<UserResource> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserResource getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toDto).orElse(null);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    @Transactional
    public void registerNewUser(RegisterRequest request) {
        final Role userRole = getDefaultUserRole();
        final User user = createNewUser(request, userRole);
        final ConfirmationToken token = createConfirmationToken(user);
        sendConfirmationEmail(request, token);
    }

    private Role getDefaultUserRole() {
        return roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Default role USER not found"));
    }

    private void sendConfirmationEmail(RegisterRequest request, ConfirmationToken token) {
        final String html = createEmail(token);
        emailService.sendEmail(request.email(), "Confirm your registration", html);
    }

    private ConfirmationToken createConfirmationToken(User user) {
        ConfirmationToken token = new ConfirmationToken(user);
        return confirmationTokenRepository.save(token);
    }

    private User createNewUser(RegisterRequest request, Role userRole) {
        final User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(userRole))
                .enabled(false)
                .build();
        return userRepository.save(user);
    }

    private static String createEmail(ConfirmationToken token) {
        String link = "http://localhost:8080/auth/confirm?token=" + token.getToken();
        return "<p>Click the link below to activate your account:</p>" +
                "<a href=\"" + link + "\">Activate Account</a>";
    }

}
