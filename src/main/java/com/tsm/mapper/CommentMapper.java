package com.tsm.mapper;

import org.springframework.stereotype.Component;
import com.tsm.dto.CommentDto;
import com.tsm.entity.Comment;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentMapper implements Mapper<Comment, CommentDto>{

    private final UserMapper userMapper;

    @Override
    public CommentDto map(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setAuthor(userMapper.map(comment.getAuthor()));
        commentDto.setCreatedAt(comment.getCreatedAt());
        return commentDto;
    }
    
    public Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setContent(commentDto.getContent());
        if (commentDto.getAuthor() != null) {
            comment.setAuthor(userMapper.mapToEntity(commentDto.getAuthor()));
        } else {
            comment.setAuthor(null);
        }
        comment.setCreatedAt(commentDto.getCreatedAt());

        return comment;
    }
}
