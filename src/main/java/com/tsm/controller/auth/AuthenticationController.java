package com.tsm.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.dto.AuthenticationRequest;
import com.tsm.dto.RegistrationUserDto;
import com.tsm.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for authentication and user registration")
public class AuthenticationController {

    private final AuthenticationService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/auth")
    @Operation(summary = "Create authentication token")
    public ResponseEntity<?> createAuthToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Authentication request body",
                required = true,
                content = @Content(schema = @Schema(implementation = AuthenticationRequest.class))
            )
            @RequestBody AuthenticationRequest authRequest) {
        logger.info("POST /api/auth - Creating authentication token");
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    @Operation(summary = "Create a new user")
    public ResponseEntity<?> createNewUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Registration user body",
                required = true,
                content = @Content(schema = @Schema(implementation = RegistrationUserDto.class))
            )
            @RequestBody RegistrationUserDto clientDto) {
        logger.info("POST /api/registration - Creating new user");
        return authService.createNewUser(clientDto);
    }
}
