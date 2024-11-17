package com.tsm.unit.service;

import com.tsm.dto.TaskDto;
import com.tsm.dto.UserDto;
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
import com.tsm.service.impl.TaskServiceImpl;
import com.tsm.service.impl.UserServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    @Test
    @DisplayName("Should create a new task successfully")
    void create_ShouldCreateTaskSuccessfully() {
        TaskDto taskDto = new TaskDto();
        User user = new User();
        UserDto userDto = new UserDto();
        Task task = new Task();

        when(userService.getCurrentUser()).thenReturn(user);
        when(userMapper.userToUserDto(user)).thenReturn(userDto);
        when(taskMapper.taskDtoToTask(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskServiceImpl.create(taskDto);

        assertNotNull(createdTask);
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should throw TaskCreationException when task creation fails")
    void create_ShouldThrowTaskCreationException_WhenTaskCreationFails() {
        TaskDto taskDto = new TaskDto();

        when(taskMapper.taskDtoToTask(taskDto)).thenReturn(null);

        assertThrows(TaskCreationException.class, () -> taskServiceImpl.create(taskDto));
    }

    @Test
    @DisplayName("Should update task successfully")
    void update_ShouldUpdateTaskSuccessfully() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);
        Task task = new Task();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        if (taskDto.getAssignee() != null) {
            User assignee = new User();
            when(userRepository.findById(taskDto.getAssignee().getId())).thenReturn(Optional.of(assignee));
        }

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        TaskDto updatedTaskDto = taskServiceImpl.update(taskDto);

        assertNotNull(updatedTaskDto);
        verify(taskRepository).save(task);
    }


    @Test
    @DisplayName("Should throw TaskNotFoundException when updating non-existent task")
    void update_ShouldThrowTaskNotFoundException_WhenTaskNotFound() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.update(taskDto));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when assignee not found")
    void update_ShouldThrowUserNotFoundException_WhenAssigneeNotFound() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setAssignee(new UserDto());

        Task task = new Task();
        Long assigneeId = 999L;

        taskDto.getAssignee().setId(assigneeId);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(assigneeId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> taskServiceImpl.update(taskDto));
    }


    @Test
    @DisplayName("Should find task by id")
    void findById_ShouldReturnTaskDto_WhenTaskFound() {
        Task task = new Task();
        TaskDto taskDto = new TaskDto();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        Optional<TaskDto> foundTask = taskServiceImpl.findById(1L);

        assertTrue(foundTask.isPresent());
        assertEquals(taskDto, foundTask.get());
    }

    @Test
    @DisplayName("Should return empty optional when task not found")
    void findById_ShouldReturnEmpty_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<TaskDto> foundTask = taskServiceImpl.findById(1L);

        assertTrue(foundTask.isEmpty());
    }

    @Test
    @DisplayName("Should delete task successfully")
    void delete_ShouldReturnTrue_WhenTaskDeleted() {
        Task task = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        boolean result = taskServiceImpl.delete(1L);

        assertTrue(result);
        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when deleting non-existent task")
    void delete_ShouldThrowTaskNotFoundException_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.delete(1L));
    }

    @Test
    @DisplayName("Should change task status successfully")
    void changeStatus_ShouldChangeTaskStatusSuccessfully() {
        Task task = new Task();
        TaskDto taskDto = new TaskDto();
        Status newStatus = Status.IN_PROGRESS;

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskServiceImpl.changeStatus(1L, newStatus);

        assertNotNull(result);
        assertEquals(newStatus, task.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when changing status of non-existent task")
    void changeStatus_ShouldThrowTaskNotFoundException_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.changeStatus(1L, Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should assign task to user successfully")
    void assignTask_ShouldAssignTaskSuccessfully() {
        Task task = new Task();
        User assignee = new User();
        assignee.setEmail("test@example.com");
        TaskDto taskDto = new TaskDto();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(assignee));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskServiceImpl.assignTask(1L, "test@example.com");

        assertNotNull(result);
        assertEquals(assignee, task.getAssignee());
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when assigning non-existent task")
    void assignTask_ShouldThrowTaskNotFoundException_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.assignTask(1L, "test@example.com"));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when assignee not found")
    void assignTask_ShouldThrowUserNotFoundException_WhenAssigneeNotFound() {
        Task task = new Task();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> taskServiceImpl.assignTask(1L, "test@example.com"));
    }

    @Test
    @DisplayName("Should throw TaskAssignmentException when task is already assigned to the user")
    void assignTask_ShouldThrowTaskAssignmentException_WhenTaskAlreadyAssigned() {
        Task task = new Task();
        User assignee = new User();
        assignee.setEmail("test@example.com");
        task.setAssignee(assignee);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(assignee));

        assertThrows(TaskAssignmentException.class, () -> taskServiceImpl.assignTask(1L, "test@example.com"));
    }

    @Test
    @DisplayName("Should find all tasks without filtering")
    void findAll_ShouldReturnAllTasks_WhenNoFilterProvided() {
        Pageable pageable = PageRequest.of(0, 10);
        Task task = new Task();
        TaskDto taskDto = new TaskDto();

        when(taskRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(task)));
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        Page<TaskDto> result = taskServiceImpl.findAll(pageable, "");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findAll(pageable);
    }
}