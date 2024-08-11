package com.tsm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.tsm.dto.CommentDto;
import com.tsm.entity.Comment;
import com.tsm.entity.Task;
import com.tsm.entity.User;
import com.tsm.exceptions.comment.CommentNotFoundException;
import com.tsm.exceptions.task.TaskNotFoundException;
import com.tsm.mapper.CommentMapper;
import com.tsm.repository.CommentRepository;
import com.tsm.repository.TaskRepository;
import com.tsm.service.CommentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserServiceImpl userService;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto addComment(Long taskId, CommentDto commentDto) {
        LOGGER.info("Adding comment to task {}: {}", taskId, commentDto);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        User author = userService.getCurrentUser();

        Comment comment = commentMapper.mapToEntity(commentDto);
        comment.setTask(task);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.map(savedComment);
    }

    @Override
    public List<CommentDto> getComments(Long taskId) {
        LOGGER.info("Retrieving comments for task {}", taskId);

        List<Comment> comments = commentRepository.findByTaskId(taskId);
        
        return comments.stream()
                .map(commentMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long commentId, CommentDto commentDto) {
        LOGGER.info("Updating comment {}: {}", commentId, commentDto);

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment  not found with id: " + commentId));

        existingComment.setContent(commentDto.getContent());

        Comment updatedComment = commentRepository.save(existingComment);

        return commentMapper.map(updatedComment);
    }

    @Override
    @Transactional
    public boolean deleteComment(Long commentId) {
        LOGGER.info("Deleting comment {}", commentId);
        return commentRepository.findById(commentId)
                .map(comment -> {
                    commentRepository.delete(comment);
                    return true;
                })
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));
    }
}