package com.tsm.unit.service;

import com.tsm.dto.CommentDto;
import com.tsm.entity.Comment;
import com.tsm.entity.Task;
import com.tsm.entity.User;
import com.tsm.exceptions.comment.CommentNotFoundException;
import com.tsm.exceptions.task.TaskNotFoundException;
import com.tsm.mapper.CommentMapper;
import com.tsm.repository.CommentRepository;
import com.tsm.repository.TaskRepository;
import com.tsm.service.impl.CommentServiceImpl;
import com.tsm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("Should add a comment to a task")
    void addComment_ShouldAddComment_WhenTaskExists() {
        Long taskId = 1L;
        CommentDto commentDto = new CommentDto();
        Task task = new Task();
        User author = new User();
        Comment comment = new Comment();
        Comment savedComment = new Comment();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userService.getCurrentUser()).thenReturn(author);
        when(commentMapper.mapToEntity(commentDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(savedComment);
        when(commentMapper.map(savedComment)).thenReturn(commentDto);

        CommentDto result = commentService.addComment(taskId, commentDto);

        assertNotNull(result);
        verify(commentRepository).save(comment);
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when task does not exist")
    void addComment_ShouldThrowTaskNotFoundException_WhenTaskDoesNotExist() {
        Long taskId = 1L;
        CommentDto commentDto = new CommentDto();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> commentService.addComment(taskId, commentDto));
    }

    @Test
    @DisplayName("Should retrieve comments for a task")
    void getComments_ShouldReturnComments_ForGivenTaskId() {
        Long taskId = 1L;
        Comment comment = new Comment();
        CommentDto commentDto = new CommentDto();

        when(commentRepository.findByTaskId(taskId)).thenReturn(List.of(comment));
        when(commentMapper.map(comment)).thenReturn(commentDto);

        List<CommentDto> result = commentService.getComments(taskId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commentDto, result.get(0));
        verify(commentRepository).findByTaskId(taskId);
    }

    @Test
    @DisplayName("Should update a comment")
    void updateComment_ShouldUpdateComment_WhenCommentExists() {
        Long commentId = 1L;
        CommentDto commentDto = new CommentDto();
        Comment existingComment = new Comment();
        Comment updatedComment = new Comment();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(updatedComment);
        when(commentMapper.map(updatedComment)).thenReturn(commentDto);

        CommentDto result = commentService.updateComment(commentId, commentDto);

        assertNotNull(result);
        verify(commentRepository).save(existingComment);
    }

    @Test
    @DisplayName("Should throw CommentNotFoundException when comment does not exist")
    void updateComment_ShouldThrowCommentNotFoundException_WhenCommentDoesNotExist() {
        Long commentId = 1L;
        CommentDto commentDto = new CommentDto();

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(commentId, commentDto));
    }

    @Test
    @DisplayName("Should delete a comment")
    void deleteComment_ShouldDeleteComment_WhenCommentExists() {
        Long commentId = 1L;
        Comment comment = new Comment();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        boolean result = commentService.deleteComment(commentId);

        assertTrue(result);
        verify(commentRepository).delete(comment);
    }

    @Test
    @DisplayName("Should throw CommentNotFoundException when comment does not exist")
    void deleteComment_ShouldThrowCommentNotFoundException_WhenCommentDoesNotExist() {
        Long commentId = 1L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(commentId));
    }
}