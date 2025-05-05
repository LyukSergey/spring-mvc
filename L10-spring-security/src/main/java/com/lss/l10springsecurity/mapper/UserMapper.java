package com.lss.l10springsecurity.mapper;

import com.lss.l10springsecurity.dto.UserDto;
import com.lss.l10springsecurity.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
