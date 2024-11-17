package com.tsm.mapper;

import com.tsm.dto.CommentDto;
import com.tsm.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    @Mapping(source = "author", target = "author") 
    CommentDto commentToCommentDto(Comment comment);

    @Mapping(source = "author", target = "author") 
    Comment commentDtoToComment(CommentDto commentDto);
}
