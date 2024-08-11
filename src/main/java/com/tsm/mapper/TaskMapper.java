package com.tsm.mapper;

import org.springframework.stereotype.Component;

import com.tsm.dto.TaskDto;
import com.tsm.entity.Task;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskMapper implements Mapper<Task, TaskDto>{

    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

    @Override
    public TaskDto map(Task task) { 
        if (task == null) {
            return null;
        }
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus());
        taskDto.setPriority(task.getPriority());
        if (taskDto.getAssignee() != null) {
            taskDto.setAssignee(userMapper.map(task.getAssignee()));
        }
        taskDto.setAuthor(userMapper.map(task.getAuthor()));
        taskDto.setComments(task.getComments() == null ? null : task.getComments().stream()
                .map(commentMapper::map)
                .toList());
        taskDto.setCreatedAt(task.getCreatedAt());
        taskDto.setUpdatedAt(task.getUpdatedAt());

        return taskDto;
    }

    public Task mapToEntity(TaskDto taskDto){
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        if (taskDto.getAssignee() != null) {
            task.setAssignee(userMapper.mapToEntity(taskDto.getAssignee()));
        }
        task.setAuthor(userMapper.mapToEntity(taskDto.getAuthor()));
        task.setComments(taskDto.getComments() == null ? null : taskDto.getComments().stream()
                .map(commentMapper::mapToEntity)
                .toList());
        task.setCreatedAt(taskDto.getCreatedAt());
        task.setUpdatedAt(taskDto.getUpdatedAt());

        return task;
    }
    
}
