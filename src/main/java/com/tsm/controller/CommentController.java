package com.tsm.controller;

import com.tsm.dto.CommentDto;
import com.tsm.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);
    private final CommentService taskCommentService;

    @Operation(summary = "Add a new comment to a task", description = "Creates a new comment for the specified task.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Comment created successfully", 
                     content = @Content(schema = @Schema(implementation = CommentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and @taskServiceImpl.canUserAccessTask(#taskId))")
    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentDto commentDto) {
        LOGGER.info("POST /tasks/{}/comments - Adding comment", taskId);
        CommentDto createdComment = taskCommentService.addComment(taskId, commentDto);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve comments for a task", description = "Fetches all comments associated with a specific task.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comments retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = CommentDto.class))),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and @taskServiceImpl.canUserAccessTask(#taskId))")
    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long taskId) {
        LOGGER.info("GET /tasks/{}/comments - Retrieving comments", taskId);
        List<CommentDto> comments = taskCommentService.getComments(taskId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @Operation(summary = "Update a comment", description = "Updates an existing comment by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comment updated successfully", 
                     content = @Content(schema = @Schema(implementation = CommentDto.class))),
        @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDto commentDto) {
        LOGGER.info("PUT /tasks/comments/{} - Updating comment", commentId);
        CommentDto updatedComment = taskCommentService.updateComment(commentId, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @Operation(summary = "Delete a comment", description = "Deletes an existing comment by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        LOGGER.info("DELETE /tasks/comments/{} - Deleting comment", commentId);
        boolean deleted = taskCommentService.deleteComment(commentId);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
