package com.tsm.service;

import java.util.List;

import com.tsm.dto.CommentDto;

public interface CommentService {
    CommentDto addComment(Long taskId, CommentDto commentDto);
    List<CommentDto> getComments(Long taskId);
    CommentDto updateComment(Long commentId, CommentDto commentDto);
    boolean deleteComment(Long commentId);
}