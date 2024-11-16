package com.tsm.service;

import com.tsm.dto.TaskDto;
import com.tsm.entity.Status;
import com.tsm.entity.Task;

import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    Task create(TaskDto taskDto);
    TaskDto update(TaskDto taskDto);
    Optional<TaskDto> findById(Long id);
    boolean delete(Long id);
    TaskDto changeStatus(Long id, Status status);
    TaskDto assignTask(Long taskId, String assigneeEmail);
    Page<TaskDto> findAll(Pageable pageable, String filter);
    boolean canUserAccessTask(Long taskId);
}