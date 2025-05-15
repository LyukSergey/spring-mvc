package com.lss.l101springsecuritydb.rest;

import com.lss.l101springsecuritydb.entity.Role;
import com.lss.l101springsecuritydb.entity.User;
import com.lss.l101springsecuritydb.repository.RoleRepository;
import com.lss.l101springsecuritydb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class VulnerableController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/admin/user")
    public ResponseEntity<String> updatePassword(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String roleName,
            @RequestParam String password) {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        final Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        user.setEmail(email);
        userRepository.save(user);

        user.setPassword(passwordEncoder.encode(password)); // ⚠️ без перевірки прав!
        userRepository.save(user);

        return ResponseEntity.ok("Password updated");
    }

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "<h1>Welcome to Target App</h1><p>You're logged in. You can be CSRF attacked.</p>";
    }
}