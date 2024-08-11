package com.tsm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing a user")
public class UserDto {

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Email address of the user", example = "user@example.com")
    private String email;

    @Schema(description = "Password of the user", example = "password123")
    private String password;
}
