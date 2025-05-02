package com.lss.l9springdataspringbootcustom.converters;

import com.lss.l9springdataspringbootcustom.dto.PostDto;
import com.lss.l9springdataspringbootcustom.entity.Post;
import com.lss.l9springdataspringbootcustom.entity.Users;

public final class PostConverter {

    public static PostDto toDto(Post post) {
        return PostDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public static Post toEntity(PostDto dto, Users user) {
        return Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();
    }
}
