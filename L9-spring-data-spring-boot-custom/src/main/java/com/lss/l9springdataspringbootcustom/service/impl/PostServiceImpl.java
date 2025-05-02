package com.lss.l9springdataspringbootcustom.service.impl;

import com.lss.l9springDataJpaCustom.annotation.MyTransactional;
import com.lss.l9springdataspringbootcustom.entity.Post;
import com.lss.l9springdataspringbootcustom.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl {

    private final PostRepository postRepository;

    @MyTransactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @MyTransactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
