package com.lss.l9springdataspringbootcustom;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/find")
    public List<Users> findByName(@RequestParam String name) {
        return userRepository.findByName(name);
    }

    @GetMapping("/{id}")
    public Users findById(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @GetMapping("/all")
    public List<Users> findAll() {
        return userRepository.findAll();
    }

}
