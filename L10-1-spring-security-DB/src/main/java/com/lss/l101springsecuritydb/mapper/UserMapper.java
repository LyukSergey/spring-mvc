package com.lss.l101springsecuritydb.mapper;

import com.lss.l101springsecuritydb.dto.UserResource;
import com.lss.l101springsecuritydb.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(r -> r.getName()).collect(java.util.stream.Collectors.toSet()))")
    UserResource toDto(User user);
}
