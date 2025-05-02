package com.lss.l9springdataspringbootcustom;

import com.lss.l9springDataJpaCustom.annotation.MyTransactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

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
