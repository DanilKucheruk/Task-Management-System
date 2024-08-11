package com.tsm.controller;

import com.tsm.dto.TaskDto;
import com.tsm.entity.Status;
import com.tsm.entity.Task;
import com.tsm.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    @Operation(summary = "Create a new task", description = "Creates a new task based on the provided data.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task created successfully", 
                     content = @Content(schema = @Schema(implementation = Task.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDto taskDto) {
        LOGGER.info("POST /tasks - Creating new task: {}", taskDto);
        Task createdTask = taskService.create(taskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing task", description = "Updates the details of an existing task.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task updated successfully", 
                     content = @Content(schema = @Schema(implementation = TaskDto.class))),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable @NotNull Long id,
            @RequestBody @Valid TaskDto taskDto) {
        LOGGER.info("PUT /tasks/{} - Updating task: {}", id, taskDto);
        taskDto.setId(id);
        TaskDto updatedTask = taskService.update(taskDto);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @Operation(summary = "Get task by ID", description = "Fetches a task by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = TaskDto.class))),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable @NotNull Long id) {
        LOGGER.info("GET /tasks/{} - Retrieving task", id);
        Optional<TaskDto> task = taskService.findById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Delete a task", description = "Deletes a task by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable @NotNull Long id) {
        LOGGER.info("DELETE /tasks/{} - Deleting task", id);
        boolean deleted = taskService.delete(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Change task status", description = "Changes the status of a task by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status updated successfully", 
                     content = @Content(schema = @Schema(implementation = TaskDto.class))),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDto> changeTaskStatus(
            @PathVariable @NotNull Long id,
            @RequestParam @NotNull Status status) {
        LOGGER.info("PATCH /tasks/{}/status - Changing status to {}", id, status);
        TaskDto updatedTask = taskService.changeStatus(id, status);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @Operation(summary = "Assign task to a user", description = "Assigns a task to a user based on their email address.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task assigned successfully", 
                     content = @Content(schema = @Schema(implementation = TaskDto.class))),
        @ApiResponse(responseCode = "404", description = "Task or user not found")
    })
    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<TaskDto> assignTask(
            @PathVariable @NotNull Long taskId,
            @RequestParam @NotNull String assigneeEmail) {
        LOGGER.info("PATCH /tasks/{}/assign - Assigning to user with email {}", taskId, assigneeEmail);
        TaskDto updatedTask = taskService.assignTask(taskId, assigneeEmail);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @Operation(summary = "Get all tasks", description = "Retrieves a paginated list of tasks with optional filtering.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<TaskDto>> getTasks(Pageable pageable, 
                                                  @RequestParam(required = false) String filter) {
        LOGGER.info("GET /tasks - Retrieving task list with filter: {}", filter);
        Page<TaskDto> tasks = taskService.findAll(pageable, filter);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

}
