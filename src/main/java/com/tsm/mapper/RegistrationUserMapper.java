package com.tsm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.mapstruct.factory.Mappers;

import com.tsm.dto.RegistrationUserDto;
import com.tsm.entity.Role;
import com.tsm.entity.User;

@Mapper(componentModel = "spring")
public interface RegistrationUserMapper {

    RegistrationUserMapper INSTANCE = Mappers.getMapper(RegistrationUserMapper.class);

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    @Mapping(source = "role", target = "role", qualifiedByName = "stringToRole")
    User registrationUserDtoToUser(RegistrationUserDto registrationUserDto);

    @Named("encodePassword")
    static String encodePassword(String password) {
        if (password != null && !password.trim().isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return passwordEncoder.encode(password);
        }
        return null;
    }

    @Named("stringToRole")
    static Role stringToRole(String role) {
        return Role.valueOf(role);
    }
}
