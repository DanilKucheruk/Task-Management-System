package com.tsm.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tsm.dto.RegistrationUserDto;
import com.tsm.dto.UserDto;
import com.tsm.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User create(RegistrationUserDto user);

    Optional<UserDto> findById(Long id);

    Optional<User> findByEmail(String email);

    List<UserDto> findAll();

    boolean delete(Long id);
}