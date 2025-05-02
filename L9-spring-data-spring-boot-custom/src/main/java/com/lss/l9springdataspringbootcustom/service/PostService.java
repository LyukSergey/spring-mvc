package com.lss.l9springdataspringbootcustom.service;

import com.lss.l9springdataspringbootcustom.entity.Post;
import java.util.List;

public interface PostService {

    Post createPost(Post post);

    void deletePost(Long id);

    List<Post> findAll();
}
