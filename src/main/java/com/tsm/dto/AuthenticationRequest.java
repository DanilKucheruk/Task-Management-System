package com.tsm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request object for authentication")
public class AuthenticationRequest {

    @Schema(description = "Email of the user", example = "user@example.com")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Password of the user", example = "password123")
    @NotBlank(message = "Password is mandatory")
    private String password;
}
