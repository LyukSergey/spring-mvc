package com.lss.l9springdataspringbootcustom.rest;

import com.lss.l9springdataspringbootcustom.dto.UserDto;
import com.lss.l9springdataspringbootcustom.service.UserService;
import com.lss.l9springdataspringbootcustom.entity.Users;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Users createUser(@RequestBody UserDto user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) String name,
            @RequestHeader(required = false, defaultValue = "false") boolean withPosts) {
        if (StringUtils.isNotBlank(name)) {
            return userService.findUsersByName(name, withPosts);
        }
        return userService.findAllUsers(withPosts);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.findUserById(id);
    }

}
