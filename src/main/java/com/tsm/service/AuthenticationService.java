package com.tsm.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.tsm.dto.AuthenticationRequest;
import com.tsm.dto.AuthenticationResponse;
import com.tsm.dto.RegistrationUserDto;
import com.tsm.exceptions.user.AuthenticationException;
import com.tsm.exceptions.user.UserNotFoundException;
import com.tsm.util.JwtTokenUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new AuthenticationException(HttpStatus.UNAUTHORIZED.value(), "Incorrect username or password"),
                    HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        com.tsm.entity.User user = userService.findByEmail(authRequest.getEmail())
        .orElseThrow(() -> new UserNotFoundException("User not found with email: " + authRequest.getEmail()));
        String token = jwtTokenUtils.generateToken(userDetails,user.getRole());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    public ResponseEntity<?> createNewUser(@Valid @RequestBody RegistrationUserDto userDto) {
        if (userService.findByEmail(userDto.getEmail()).isPresent()) {
            return new ResponseEntity<>(new AuthenticationException(HttpStatus.BAD_REQUEST.value(),
                    "The user with this email already exists"), HttpStatus.BAD_REQUEST);
        }
        userService.create(userDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}