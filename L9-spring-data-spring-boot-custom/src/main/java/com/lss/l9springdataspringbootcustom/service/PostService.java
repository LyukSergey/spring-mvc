package com.lss.l9springdataspringbootcustom.service;

import com.lss.l9springdataspringbootcustom.dto.PostDto;
import java.util.List;

public interface PostService {

    PostDto createPostForUser(Long userId, PostDto dto);

    void deletePost(Long id);

    List<PostDto> findAll();
}
