package com.tsm.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tsm.dto.TaskDto;
import com.tsm.entity.Task;
import com.tsm.mapper.TaskMapper;
import com.tsm.repository.TaskRepository;
import com.tsm.service.TaskSearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskSearchServiceImpl implements TaskSearchService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;  

    @Override
    public Page<TaskDto> findByAuthor(Long authorId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByAuthorId(authorId, pageable);
        return tasks.map(taskMapper::map);
    }

    @Override
    public Page<TaskDto> findByAssignee(Long assigneeId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByAssigneeId(assigneeId, pageable);
        return tasks.map(taskMapper::map);
    }

    @Override
    public Page<TaskDto> findByStatus(String status, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByStatus(status, pageable);
        return tasks.map(taskMapper::map);
    }

    @Override
    public Page<TaskDto> findByPriority(String priority, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByPriority(priority, pageable);
        return tasks.map(taskMapper::map);
    }
}
