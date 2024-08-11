package com.tsm.unit.controller.auth;

import com.tsm.controller.auth.AuthenticationController;
import com.tsm.dto.AuthenticationRequest;
import com.tsm.dto.RegistrationUserDto;
import com.tsm.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private AuthenticationController authController;

    @Test
    public void createAuthToken_ShouldReturnAuthToken() {
        AuthenticationRequest authRequest = new AuthenticationRequest("username", "password");
        ResponseEntity<?> expectedResponse = ResponseEntity.ok().build();
        when(authService.createAuthToken(authRequest)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = authController.createAuthToken(authRequest);

        assertEquals(expectedResponse, response);
        verify(authService, times(1)).createAuthToken(authRequest);
    }

    @Test
    public void createNewUser_ShouldReturnNewUser() {
        RegistrationUserDto userDto = new RegistrationUserDto(9999L, "username", "password");
        ResponseEntity<?> expectedResponse = ResponseEntity.ok().build();
        when(authService.createNewUser(userDto)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = authController.createNewUser(userDto);

        assertEquals(expectedResponse, response);
        verify(authService, times(1)).createNewUser(userDto);
    }


}