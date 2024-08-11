package com.tsm.dto;

import com.tsm.entity.Priority;
import com.tsm.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing a task")
public class TaskDto {

    @Schema(description = "Unique identifier of the task", example = "1")
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Schema(description = "Title of the task", example = "Implement feature X")
    private String title;

    @NotBlank(message = "Description is mandatory")
    @Schema(description = "Description of the task", example = "Detailed description of what needs to be done for feature X")
    private String description;

    @NotNull(message = "Status is mandatory")
    @Schema(description = "Current status of the task", example = "IN_PROGRESS")
    private Status status;

    @NotNull(message = "Priority is mandatory")
    @Schema(description = "Priority of the task", example = "HIGH")
    private Priority priority;

    @Schema(description = "Author of the task")
    private UserDto author;

    @Schema(description = "Assignee of the task")
    private UserDto assignee;

    @Schema(description = "List of comments associated with the task")
    private List<@Valid CommentDto> comments;

    @Schema(description = "Timestamp when the task was created", example = "2023-08-11T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the task was last updated", example = "2023-08-12T12:00:00")
    private LocalDateTime updatedAt;
}
