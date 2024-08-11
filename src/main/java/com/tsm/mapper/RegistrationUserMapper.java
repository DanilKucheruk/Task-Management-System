package com.tsm.mapper;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tsm.dto.RegistrationUserDto;
import com.tsm.entity.User;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class RegistrationUserMapper implements Mapper<RegistrationUserDto, User>{
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User map(RegistrationUserDto object) {
        User user = new User();
        user.setEmail(object.getEmail());
        Optional.ofNullable(object.getPassword()).filter(StringUtils::hasText)
        .map(passwordEncoder::encode).ifPresent(user::setPassword);
        return user;
    }
    
}
