package com.tsm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request object for user registration")
public class RegistrationUserDto {

    @Schema(description = "ID of the user", example = "1")
    private Long id;

    @Schema(description = "Email of the new user", example = "newuser@example.com")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Password for the new user", example = "password123")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 4, message = "Password should be at least 4 characters")
    private String password;

    @Schema(description = "Role for the new user", example = "ROLE_USER")
    @NotBlank(message = "Role is mandatory")
    private String role;
}
