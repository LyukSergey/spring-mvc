package com.lss.l10springsecurity.rest;

import com.lss.l10springsecurity.dto.PostDto;
import com.lss.l10springsecurity.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/keycloak/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/user/{userId}")
    public PostDto createPost(@PathVariable Long userId, @RequestBody PostDto post) {
        return postService.createPost(userId, post);
    }

    @GetMapping("/user/{userId}")
    public List<PostDto> getUserPosts(@PathVariable Long userId) {
        return postService.getPostsByUser(userId);
    }
}
