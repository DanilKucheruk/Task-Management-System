package com.tsm.service.impl;

import com.tsm.dto.RegistrationUserDto;
import com.tsm.dto.UserDto;
import com.tsm.entity.User;
import com.tsm.exceptions.user.UserAlreadyExistsException;
import com.tsm.exceptions.user.UserCreationException;
import com.tsm.exceptions.user.UserNotFoundException;
import com.tsm.mapper.RegistrationUserMapper;
import com.tsm.mapper.UserMapper;
import com.tsm.repository.UserRepository;
import com.tsm.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RegistrationUserMapper registrationUserMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Request to load user by email: {}", email);
    
        return userRepository.findByEmail(email)
                .map(user -> {
                    GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name()); // Одиночная роль
                    List<GrantedAuthority> authorities = Collections.singletonList(authority);
    
                    return new org.springframework.security.core.userdetails.User(
                            user.getEmail(),
                            user.getPassword(),
                            authorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Failed to retrieve user with email: " + email));
    }

    @Override
    @Transactional
    public User create(RegistrationUserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + userDto.getEmail() + " already exists");
        }
        return Optional.of(userDto)
                .map(dto -> {
                    return registrationUserMapper.map(dto);
                })
                .map(userRepository::save)
                .orElseThrow(() -> new UserCreationException("Failed to create user"));
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        LOGGER.debug("Request to find user by id: {}", id);
        return userRepository.findById(id)
                .map(userMapper::map)
                .or(() -> {
                    throw new UserNotFoundException("User with id " + id + " not found");
                });
    }

    @Override
    public Optional<User> findByEmail(String email) {
        LOGGER.debug("Request to find client by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAll() {
        LOGGER.debug("Request to find all users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::map)
                .toList();
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        LOGGER.debug("Request to user client by id: {}", id);
        return userRepository.findById(id)
                .map(entity -> {
                    if (entity != null) {
                        userRepository.delete(entity);
                        userRepository.flush();
                        return true;
                    } else {
                        return false;
                    }
                })
                .orElse(false);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("The user is not authenticated or there is no authentication");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}