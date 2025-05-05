package com.lss.l10springsecurity.service;

import com.lss.l10springsecurity.dto.PostDto;
import com.lss.l10springsecurity.entity.Post;
import com.lss.l10springsecurity.entity.User;
import com.lss.l10springsecurity.mapper.PostMapper;
import com.lss.l10springsecurity.repository.PostRepository;
import com.lss.l10springsecurity.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @PreAuthorize("hasRole('admin')")
    public PostDto createPost(Long userId, PostDto postDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postMapper.toEntity(postDto);
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        return postMapper.toDto(savedPost);
    }

    @PreAuthorize("hasAnyRole('moderator')")
    public List<PostDto> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(postMapper::toDto)
                .toList();
    }
}
