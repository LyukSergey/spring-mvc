package com.lss.l9springdataspringbootcustom.service.impl;

import com.lss.l9springDataJpaCustom.annotation.MyTransactional;
import com.lss.l9springdataspringbootcustom.converters.PostConverter;
import com.lss.l9springdataspringbootcustom.dto.PostDto;
import com.lss.l9springdataspringbootcustom.entity.Post;
import com.lss.l9springdataspringbootcustom.entity.Users;
import com.lss.l9springdataspringbootcustom.repository.PostRepository;
import com.lss.l9springdataspringbootcustom.repository.UserRepository;
import com.lss.l9springdataspringbootcustom.service.PostService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @MyTransactional
    public PostDto createPostForUser(Long userId, PostDto dto) {
        Users user = Optional.ofNullable(userRepository.findById(userId))
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id: %s", userId)));
        final Post saved = postRepository.save(PostConverter.toEntity(dto, user));
//        throw new RuntimeException("Some error test");
        return PostConverter.toDto(saved);
    }

    @MyTransactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<PostDto> findAll() {
        return postRepository.findAll().stream()
                .map(PostConverter::toDto)
                .toList();
    }
}
