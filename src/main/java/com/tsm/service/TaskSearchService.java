package com.tsm.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.tsm.dto.TaskDto;

public interface TaskSearchService {
    Page<TaskDto> findByAuthor(Long authorId, Pageable pageable);
    Page<TaskDto> findByAssignee(Long assigneeId, Pageable pageable);
    Page<TaskDto> findByStatus(String status, Pageable pageable);
    Page<TaskDto> findByPriority(String priority, Pageable pageable);
    Page<TaskDto> findAll(Pageable pageable, String filter);
}