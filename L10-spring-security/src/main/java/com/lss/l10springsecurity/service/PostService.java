package com.lss.l10springsecurity.service;

import com.lss.l10springsecurity.entity.Post;
import com.lss.l10springsecurity.entity.User;
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

    @PreAuthorize("hasRole('admin')")
    public Post createPost(Long userId, Post post) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setUser(user);
        return postRepository.save(post);
    }

    @PreAuthorize("hasAnyRole('admin', 'moderator')")
    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }
}
