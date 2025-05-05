package com.lss.l10springsecurity.mapper;

import com.lss.l10springsecurity.dto.PostDto;
import com.lss.l10springsecurity.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "user.id", target = "userId")
    PostDto toDto(Post post);

    @Mapping(source = "userId", target = "user.id")
    Post toEntity(PostDto postDto);
}
