package com.tsm.controller;

import com.tsm.dto.UserDto;
import com.tsm.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.Optional;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Operation(summary = "Retrieve a user by ID", description = "Fetch a user's details using their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found and returned successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID of the user to be retrieved") @PathVariable @NotNull Long id) {
        LOGGER.info("GET /users/{} - Retrieving user", id);
        Optional<UserDto> userDto = userService.findById(id);
        return userDto.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Delete a user by ID", description = "Remove a user from the system using their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to be deleted") @PathVariable @NotNull Long id) {
        LOGGER.info("DELETE /users/{} - Deleting user", id);
        boolean deleted = userService.delete(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
