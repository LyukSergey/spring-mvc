package com.lss.l101springsecuritydb.rest.view;

import com.lss.l101springsecuritydb.dto.RegisterRequest;
import com.lss.l101springsecuritydb.entity.ConfirmationToken;
import com.lss.l101springsecuritydb.repository.ConfirmationTokenRepository;
import com.lss.l101springsecuritydb.repository.UserRepository;
import com.lss.l101springsecuritydb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthViewController {

    private final UserService userService;
    private final ConfirmationTokenRepository tokenRepo;
    private final UserRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userService.existsByUsername(request.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        }

        userService.registerNewUser(request);
        return ResponseEntity.ok("Registration successful");
    }

    @GetMapping("/confirm")
    public String confirmUser(@RequestParam("token") String token) {
        final ConfirmationToken confirmationToken = tokenRepo.findByToken(token)
                .orElse(null);
        if (confirmationToken == null) {
            return "redirect:/login?error";
        }

        confirmationToken.getUser().setEnabled(true);
        userRepo.save(confirmationToken.getUser());

        // Видаляємо токен
        tokenRepo.delete(confirmationToken);
        return "redirect:/login?confirmed";
    }


}
