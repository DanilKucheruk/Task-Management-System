package com.tsm.repository;

import com.tsm.entity.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByTitleContainingOrDescriptionContainingOrStatusContaining(
            String title, String description, String status, Pageable pageable);
    Page<Task> findAll(Pageable pageable);
    Page<Task> findByAuthorId(Long authorId, Pageable pageable);
    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);
    Page<Task> findByStatus(String status, Pageable pageable);
    Page<Task> findByPriority(String priority, Pageable pageable);
}