package com.lss.l9springdataspringbootcustom;

import com.lss.l9springDataJpaCustom.MyTransactional;
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
        return userRepository.save(createUser(user));
    }

    @MyTransactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @MyTransactional
    public List<UserDto> findAllUsers() {
        final List<Users> users = userRepository.findAll();
        System.out.println("session is closed now");  // session закривається після repo call

        users.forEach(u -> u.getPosts().size());
        return users.stream()
                .map(this::convertToDto)
                .toList();
    }

    public UserDto findUserById(Long id) {
        return Optional.ofNullable(userRepository.findById(id))
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException(String.format("User with %s id not found", id)));
    }

    public List<UserDto> findUsersByName(String name) {
        return userRepository.findByName(name).stream()
                .map(this::convertToDto)
                .toList();
    }

    private UserDto convertToDto(Users user) {
        final List<PostDto> posts = user.getPosts().stream()
                .map(UserServiceImpl::convertToPostDto)
                .toList();
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .posts(posts)
                .build();
    }

    private static PostDto convertToPostDto(Post post) {
        return PostDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    private Users createUser(UserDto dto) {
        return Users.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();

    }


}
