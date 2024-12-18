package com.tsm.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.tsm.dto.AuthenticationRequest;
import com.tsm.dto.AuthenticationResponse;
import com.tsm.dto.RegistrationUserDto;
import com.tsm.entity.User;
import com.tsm.entity.Role;
import com.tsm.exceptions.user.AuthenticationException;
import com.tsm.service.AuthenticationService;
import com.tsm.service.UserService;
import com.tsm.util.JwtTokenUtils;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Should generate token when authentication is successful")
    void testCreateAuthToken_SuccessfulAuthentication_ShouldReturnToken() {
        // Arrange
        AuthenticationRequest authRequest = new AuthenticationRequest("user@gmail.com", "password123");
        UserDetails userDetails = mock(UserDetails.class);
        User user = mock(User.class);
        when(userService.loadUserByUsername(authRequest.getEmail())).thenReturn(userDetails);
        when(userService.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtTokenUtils.generateToken(userDetails, user.getRole())).thenReturn("valid-token");

        // Act
        ResponseEntity<?> response = authenticationService.createAuthToken(authRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthenticationResponse);
        AuthenticationResponse authResponse = (AuthenticationResponse) response.getBody();
        assertEquals("valid-token", authResponse.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).loadUserByUsername(authRequest.getEmail());
        verify(jwtTokenUtils).generateToken(userDetails, user.getRole());
    }

    @Test
    @DisplayName("Should return Unauthorized status when authentication fails due to incorrect credentials")
    void testCreateAuthToken_IncorrectCredentials_ShouldReturnUnauthorized() {
        // Arrange
        AuthenticationRequest authRequest = new AuthenticationRequest("user@example.com", "wrongpassword");
        doThrow(new BadCredentialsException("Incorrect username or password"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act
        ResponseEntity<?> response = authenticationService.createAuthToken(authRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthenticationException);
        AuthenticationException exception = (AuthenticationException) response.getBody();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), exception.getStatus());
        assertEquals("Incorrect username or password", exception.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, never()).loadUserByUsername(authRequest.getEmail());
        verify(jwtTokenUtils, never()).generateToken(any(UserDetails.class),any(Role.class) );
    }

    @Test
    @DisplayName("Should return Bad Request when user already exists with the given email")
    void testCreateNewUser_UserAlreadyExists_ShouldReturnBadRequest() {
        // Arrange
        RegistrationUserDto userDto = new RegistrationUserDto(null, "existinguser@example.com", "password123", "ADMIN");
        when(userService.findByEmail(userDto.getEmail())).thenReturn(Optional.of(mock(User.class)));

        // Act
        ResponseEntity<?> response = authenticationService.createNewUser(userDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthenticationException);
        AuthenticationException exception = (AuthenticationException) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatus());
        assertEquals("The user with this email already exists", exception.getMessage());

        verify(userService).findByEmail(userDto.getEmail());
        verify(userService, never()).create(any(RegistrationUserDto.class));
    }

    @Test
    @DisplayName("Should return Created status when a new user is successfully created")
    public void createNewUser_ShouldReturnCreated_WhenUserIsCreated() {
        // Arrange
        RegistrationUserDto userDto = new RegistrationUserDto(null, "newuser@example.com", "password123", "ADMIN");
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setEmail("newuser@example.com");
        createdUser.setPassword("password123");

        when(userService.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(userService.create(any(RegistrationUserDto.class))).thenReturn(createdUser);

        // Act
        ResponseEntity<?> response = authenticationService.createNewUser(userDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.CREATED, response.getBody()); 
        verify(userService).findByEmail(userDto.getEmail());
        verify(userService).create(userDto);
    }
}
