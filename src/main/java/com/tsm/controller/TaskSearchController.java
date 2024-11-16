package com.tsm.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.dto.TaskDto;
import com.tsm.service.TaskSearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("api/tasks/search")
@RequiredArgsConstructor
public class TaskSearchController {

    private final TaskSearchService taskSearchService;

    @Operation(summary = "Find tasks by author", description = "Retrieve a paginated list of tasks created by a specific author.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/by-author")
    public Page<TaskDto> findByAuthor(
            @Parameter(description = "ID of the author") @RequestParam Long authorId, 
            @Parameter(description = "Pagination information") Pageable pageable) {
        return taskSearchService.findByAuthor(authorId, pageable);
    }

    @Operation(summary = "Find tasks by assignee", description = "Retrieve a paginated list of tasks assigned to a specific user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Assignee not found")
    })
    @GetMapping("/by-assignee")
    public Page<TaskDto> findByAssignee(
            @Parameter(description = "ID of the assignee") @RequestParam Long assigneeId, 
            @Parameter(description = "Pagination information") Pageable pageable) {
        return taskSearchService.findByAssignee(assigneeId, pageable);
    }

    @Operation(summary = "Find tasks by status", description = "Retrieve a paginated list of tasks filtered by status.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Status not found")
    })
    @GetMapping("/by-status")
    public Page<TaskDto> findByStatus(
            @Parameter(description = "Status of the tasks (e.g., OPEN, CLOSED)") @RequestParam String status, 
            @Parameter(description = "Pagination information") Pageable pageable) {
        return taskSearchService.findByStatus(status, pageable);
    }

    @Operation(summary = "Find tasks by priority", description = "Retrieve a paginated list of tasks filtered by priority.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Priority not found")
    })
    @GetMapping("/by-priority")
    public Page<TaskDto> findByPriority(
            @Parameter(description = "Priority of the tasks (e.g., HIGH, MEDIUM, LOW)") @RequestParam String priority, 
            @Parameter(description = "Pagination information") Pageable pageable) {
        return taskSearchService.findByPriority(priority, pageable);
    }
}
