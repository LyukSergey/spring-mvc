package com.lss.l9springdataspringbootcustom.rest;

import com.lss.l9springdataspringbootcustom.dto.PostDto;
import com.lss.l9springdataspringbootcustom.service.PostService;
import com.lss.l9springdataspringbootcustom.service.impl.PostServiceImpl;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @PostMapping("/users/{id}")
    public PostDto createPost(@PathVariable Long id, @RequestBody PostDto post) {
        return postService.createPostForUser(id, post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @GetMapping
    public List<PostDto> findAll() {
        return postService.findAll();
    }
}
