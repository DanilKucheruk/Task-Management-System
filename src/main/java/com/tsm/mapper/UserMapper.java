package com.tsm.mapper;

import com.tsm.dto.UserDto;
import com.tsm.entity.Role;
import com.tsm.entity.User;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "role", target = "role", qualifiedByName = "roleToString")
    UserDto userToUserDto(User user);

    @Mapping(source = "role", target = "role", qualifiedByName = "stringToRole")
    User userDtoToUser(UserDto userDto);

    @Named("roleToString")
    default String roleToString(Role role) {
        return role.name();
    }

    @Named("stringToRole")
    default Role stringToRole(String role) {
        return Role.valueOf(role);
    }
}
