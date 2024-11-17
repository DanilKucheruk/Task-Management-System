package com.tsm.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.tsm.dto.TaskDto;
import com.tsm.entity.Status;
import com.tsm.entity.Task;
import com.tsm.entity.User;
import com.tsm.exceptions.task.TaskAssignmentException;
import com.tsm.exceptions.task.TaskCreationException;
import com.tsm.exceptions.task.TaskNotFoundException;
import com.tsm.exceptions.user.UserNotFoundException;
import com.tsm.mapper.TaskMapper;
import com.tsm.mapper.UserMapper;
import com.tsm.repository.TaskRepository;
import com.tsm.repository.UserRepository;
import com.tsm.repository.specification.TaskSpecification;
import com.tsm.service.TaskService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class); 
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;
    
    @Override
    @Transactional
    public Task create(TaskDto taskDto) {
        LOGGER.info("Creating a new task: {}", taskDto);
        return Optional.of(taskDto)
                .map(dto -> {
                    dto.setAuthor(userMapper.userToUserDto(userService.getCurrentUser()));
                    return taskMapper.taskDtoToTask(dto);
                })
                .map(taskRepository::save)
                .orElseThrow(() -> new TaskCreationException("Failed to retrieve task with id: " + taskDto.getId()));
    }
   
    @Override
    @Transactional
    public TaskDto update(TaskDto taskDto) {
        LOGGER.info("Updating task: {}", taskDto);

        return Optional.of(taskDto)
                .map(dto -> {
                    Task existingTask = taskRepository.findById(dto.getId())
                           .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskDto.getId()));
        
    
                    existingTask.setTitle(dto.getTitle());
                    existingTask.setDescription(dto.getDescription());
                    existingTask.setStatus(dto.getStatus());
                    existingTask.setPriority(dto.getPriority());
    
                    if (dto.getAssignee() != null) {
                        User assignee = userRepository.findById(dto.getAssignee().getId())
                                 .orElseThrow(() -> new UserNotFoundException("Assignee not found with id: " + taskDto.getAssignee().getId()));
                        existingTask.setAssignee(assignee);
                    }
    
                    return taskRepository.save(existingTask);
                })
                .map(taskMapper::taskToTaskDto)
                .orElseThrow(() -> new TaskCreationException("Update failed, no task found"));
    }

    @Override
    public Optional<TaskDto> findById(Long id) {
        LOGGER.debug("Finding task by id {}", id);
        return taskRepository.findById(id).map(taskMapper::taskToTaskDto);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        LOGGER.debug("Deleting task with id {}", id);
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return true;
                })
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    @Override
    @Transactional
    public TaskDto changeStatus(Long id, Status status) {
        LOGGER.info("Changing status of task with id {} to {}", id, status);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);

        return taskMapper.taskToTaskDto(updatedTask);
    }

    @Override
    @Transactional
    public TaskDto assignTask(Long taskId, String assigneeEmail) {
        LOGGER.info("Assigning task with id {} to user with email {}", taskId, assigneeEmail);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        User assignee = userRepository.findByEmail(assigneeEmail)
                    .orElseThrow(() -> new UserNotFoundException("User not found with email: " + assigneeEmail));

        if (task.getAssignee() != null && task.getAssignee().equals(assignee)) {
            throw new TaskAssignmentException("Task is already assigned to this user");
        }

        task.setAssignee(assignee);

        Task updatedTask = taskRepository.save(task);

        return taskMapper.taskToTaskDto(updatedTask);
    }

    
    @Override
    public Page<TaskDto> findAll(Pageable pageable, String filter) {
        Specification<Task> spec = TaskSpecification.searchByFilter(filter); 
        Page<Task> tasks;
        if (StringUtils.hasText(filter)) {
            tasks = taskRepository.findAll(spec, pageable); 
        } else {
            tasks = taskRepository.findAll(pageable); 
        }
        return tasks.map(taskMapper::taskToTaskDto);
    }    

    @Override   
    public boolean canUserAccessTask(Long taskId) {
        TaskDto taskDto = findById(taskId).orElse(null);
        if (taskDto != null && taskDto.getAssignee() != null) {
            User currentUser = userService.getCurrentUser();
            return currentUser.getEmail().equals(taskDto.getAssignee().getEmail());
        }

        return false;
    }
}
