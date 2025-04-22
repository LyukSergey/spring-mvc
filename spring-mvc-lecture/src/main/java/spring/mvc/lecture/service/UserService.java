package spring.mvc.lecture.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.mvc.lecture.UserNotFoundException;
import spring.mvc.lecture.entity.User;
import spring.mvc.lecture.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(String name, String email) {
        final User user = new User();
        user.setName(name);
        user.setEmail(email);
        userRepository.createUser(user);
        return user;
    }

    public User updateUser(int id, String name, String email) {
        userRepository.updateUser(id, name, email);
        return getUserById(id);
    }

    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }

    public User getUserById(long id) {
        return userRepository.findById(id).stream().findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id)));
    }

}
