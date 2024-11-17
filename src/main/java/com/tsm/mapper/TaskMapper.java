package com.tsm.mapper;

import com.tsm.dto.TaskDto;
import com.tsm.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(source = "assignee", target = "assignee") 
    @Mapping(source = "author", target = "author")     
    @Mapping(source = "comments", target = "comments") 
    TaskDto taskToTaskDto(Task task);

    @Mapping(source = "assignee", target = "assignee") 
    @Mapping(source = "author", target = "author")     
    @Mapping(source = "comments", target = "comments") 
    Task taskDtoToTask(TaskDto taskDto);
}
