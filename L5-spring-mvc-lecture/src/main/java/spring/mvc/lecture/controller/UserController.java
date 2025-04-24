package spring.mvc.lecture.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.mvc.lecture.entity.User;
import spring.mvc.lecture.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        System.out.printf("Request in %s listUsers()%n", this.getClass().getSimpleName());
        final List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        return "users";
    }

    @GetMapping("/json")
    @ResponseBody
    public List<User> listUsersJson() {
        System.out.printf("Request in %s listUsers() as JSON%n", this.getClass().getSimpleName());
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getUser(@PathVariable int id) {
        System.out.println("Get user ID=" + id);
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseBody
    public User createUser(@RequestParam String name, @RequestParam String email) {
        System.out.println("Creating user: name=" + name + ", email=" + email);
        return userService.createUser(name, email);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public User updateUser(@PathVariable int id, @RequestParam String name, @RequestParam String email) {
        System.out.println("Updating user ID=" + id + " with name=" + name + ", email=" + email);
        return userService.updateUser(id, name, email);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable int id) {
        System.out.println("Deleting user ID=" + id);
        userService.deleteUser(id);
        return "User deleted: " + id;
    }
}
