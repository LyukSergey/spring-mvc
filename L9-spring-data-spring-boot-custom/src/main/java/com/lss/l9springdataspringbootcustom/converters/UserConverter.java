package com.lss.l9springdataspringbootcustom.converters;

import com.lss.l9springdataspringbootcustom.dto.PostDto;
import com.lss.l9springdataspringbootcustom.dto.UserDto;
import com.lss.l9springdataspringbootcustom.entity.Post;
import com.lss.l9springdataspringbootcustom.entity.Users;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserConverter {

    public static Users toEntity(UserDto dto) {
        return Users.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserDto toDto(Users user, boolean withPosts) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .posts(getPostsDto(user.getPosts(), withPosts))
                .build();
    }

    private static List<PostDto> getPostsDto(List<Post> posts, boolean withPosts) {
        if (withPosts) {
            return posts.stream()
                    .map(PostConverter::toDto)
                    .toList();
        } else {
            return List.of();
        }
    }
}
