package com.tsm.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tsm.dto.RegistrationUserDto;
import com.tsm.dto.UserDto;
import com.tsm.entity.User;
import com.tsm.exceptions.user.UserAlreadyExistsException;
import com.tsm.exceptions.user.UserCreationException;
import com.tsm.mapper.RegistrationUserMapper;
import com.tsm.mapper.UserMapper;
import com.tsm.repository.UserRepository;
import com.tsm.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegistrationUserMapper registrationUserMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("Should load user by email successfully")
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userServiceImpl.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found by email")
    void loadUserByUsername_ShouldThrowUsernameNotFoundException_WhenUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userServiceImpl.loadUserByUsername("test@example.com");
        });

        assertEquals("Failed to retrieve user with email: test@example.com", exception.getMessage());
    }

    @Test
    @DisplayName("Should create new user successfully")
    void create_ShouldReturnUser_WhenUserIsCreatedSuccessfully() {
        RegistrationUserDto userDto = new RegistrationUserDto(null, "test@example.com", "password");
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(registrationUserMapper.map(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userServiceImpl.create(userDto);

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
    }

    @Test
    @DisplayName("Should return list of UserDto when users are found")
    void findAll_ShouldReturnListOfUserDto_WhenUsersAreFound() {
        User user = new User();
        UserDto userDto = new UserDto();
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.map(user)).thenReturn(userDto);

        List<UserDto> result = userServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDto, result.get(0));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when user with email already exists")
    void create_ShouldThrowUserAlreadyExistsException_WhenUserAlreadyExists() {
        RegistrationUserDto userDto = new RegistrationUserDto(null, "test@example.com", "password");
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(new User()));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userServiceImpl.create(userDto);
        });

        assertEquals("User with email test@example.com already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw UserCreationException when user creation fails")
    void create_ShouldThrowUserCreationException_WhenUserCreationFails() {
        RegistrationUserDto userDto = new RegistrationUserDto(null, "test@example.com", "password");
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(registrationUserMapper.map(userDto)).thenThrow(UserCreationException.class);

        assertThrows(UserCreationException.class, () -> {
            userServiceImpl.create(userDto);
        });
    }

    @Test
    @DisplayName("Should return user when found by email")
    void findByEmail_ShouldReturnUser_WhenUserIsFound() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userServiceImpl.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when user is not found by email")
    void findByEmail_ShouldReturnEmpty_WhenUserIsNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userServiceImpl.findByEmail("test@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should delete user by ID and return true when user is found")
    void delete_ShouldReturnTrue_WhenUserIsDeleted() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        boolean result = userServiceImpl.delete(1L);

        assertTrue(result);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Should return false when user is not found by ID")
    void delete_ShouldReturnFalse_WhenUserIsNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = userServiceImpl.delete(1L);

        assertFalse(result);
    }

}