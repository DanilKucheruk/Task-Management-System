package com.tsm.unit.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.tsm.controller.CommentController;
import com.tsm.dto.CommentDto;
import com.tsm.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class CommentControllerTest {

    private final CommentService commentService = mock(CommentService.class);
    private final CommentController commentController = new CommentController(commentService);

    @Test
    @DisplayName("Should add a comment successfully")
    void addComment_ShouldReturnCreatedComment() {
        Long taskId = 1L;
        CommentDto commentDto = new CommentDto();
        CommentDto createdCommentDto = new CommentDto();
        when(commentService.addComment(anyLong(), any(CommentDto.class))).thenReturn(createdCommentDto);

        ResponseEntity<CommentDto> response = commentController.addComment(taskId, commentDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdCommentDto, response.getBody());
        verify(commentService).addComment(taskId, commentDto);
    }

    @Test
    @DisplayName("Should retrieve comments for a task successfully")
    void getComments_ShouldReturnListOfComments() {
        Long taskId = 1L;
        CommentDto comment1 = new CommentDto();
        CommentDto comment2 = new CommentDto();
        List<CommentDto> comments = Arrays.asList(comment1, comment2);
        when(commentService.getComments(anyLong())).thenReturn(comments);

        ResponseEntity<List<CommentDto>> response = commentController.getComments(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(comments, response.getBody());
        verify(commentService).getComments(taskId);
    }

    @Test
    @DisplayName("Should return empty list when no comments exist")
    void getComments_ShouldReturnEmptyListWhenNoCommentsExist() {
        Long taskId = 1L;
        when(commentService.getComments(anyLong())).thenReturn(Collections.emptyList());

        ResponseEntity<List<CommentDto>> response = commentController.getComments(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(commentService).getComments(taskId);
    }

    @Test
    @DisplayName("Should update a comment successfully")
    void updateComment_ShouldReturnUpdatedComment() {
        Long commentId = 1L;
        CommentDto commentDto = new CommentDto();
        CommentDto updatedCommentDto = new CommentDto();
        when(commentService.updateComment(anyLong(), any(CommentDto.class))).thenReturn(updatedCommentDto);

        ResponseEntity<CommentDto> response = commentController.updateComment(commentId, commentDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedCommentDto, response.getBody());
        verify(commentService).updateComment(commentId, commentDto);
    }

    @Test
    @DisplayName("Should delete a comment successfully")
    void deleteComment_ShouldReturnNoContent_WhenCommentIsDeleted() {
        Long commentId = 1L;
        when(commentService.deleteComment(anyLong())).thenReturn(true);

        ResponseEntity<Void> response = commentController.deleteComment(commentId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(commentService).deleteComment(commentId);
    }

    @Test
    @DisplayName("Should return Not Found when comment to delete does not exist")
    void deleteComment_ShouldReturnNotFound_WhenCommentDoesNotExist() {
        Long commentId = 1L;
        when(commentService.deleteComment(anyLong())).thenReturn(false);

        ResponseEntity<Void> response = commentController.deleteComment(commentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(commentService).deleteComment(commentId);
    }
}
