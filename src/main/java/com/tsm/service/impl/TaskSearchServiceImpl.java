package com.tsm.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tsm.dto.TaskDto;
import com.tsm.entity.Task;
import com.tsm.mapper.TaskMapper;
import com.tsm.repository.TaskRepository;
import com.tsm.repository.specification.TaskSpecification;
import com.tsm.service.TaskSearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskSearchServiceImpl implements TaskSearchService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;  

   @Override
    public Page<TaskDto> findByAuthor(Long authorId, Pageable pageable) {
        Specification<Task> spec = (root, query, builder) -> builder.equal(root.get("author").get("id"), authorId);
        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        return tasks.map(taskMapper::map);
    }

    @Override
    public Page<TaskDto> findByAssignee(Long assigneeId, Pageable pageable) {
        Specification<Task> spec = (root, query, builder) -> builder.equal(root.get("assignee").get("id"), assigneeId);
        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        return tasks.map(taskMapper::map);
    }

    @Override
    public Page<TaskDto> findByStatus(String status, Pageable pageable) {
        Specification<Task> spec = TaskSpecification.hasTitleOrDescriptionOrStatus(null, null, status);
        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        return tasks.map(taskMapper::map);
    }

    @Override
    public Page<TaskDto> findByPriority(String priority, Pageable pageable) {
        Specification<Task> spec = (root, query, builder) -> builder.equal(root.get("priority"), priority);
        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        return tasks.map(taskMapper::map);
    }

    @Override
    public Page<TaskDto> findAll(Pageable pageable, String filter) {
        Specification<Task> spec = TaskSpecification.searchByFilter(filter); 
        Page<Task> tasks = taskRepository.findAll(spec, pageable); 
        return tasks.map(taskMapper::map); 
    }
    
}
