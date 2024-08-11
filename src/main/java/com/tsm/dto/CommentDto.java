package com.tsm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing a comment on a task")
public class CommentDto {

    @Schema(description = "Unique identifier of the comment", example = "1")
    private Long id;

    @NotBlank(message = "Content is mandatory")
    @Schema(description = "Content of the comment", example = "This is a sample comment.")
    private String content;

    @Schema(description = "Author of the comment")
    private UserDto author;

    @Schema(description = "Timestamp when the comment was created", example = "2023-08-11T10:15:30")
    private LocalDateTime createdAt;
}
