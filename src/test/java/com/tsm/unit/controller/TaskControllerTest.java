package com.tsm.unit.controller;

import com.tsm.controller.TaskController;
import com.tsm.dto.TaskDto;
import com.tsm.entity.Task;
import com.tsm.service.TaskService;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @Test
    @DisplayName("Should create a new task successfully by admin")
    void createTask_ShouldReturnCreatedTask_WhenUserIsAdmin() {
        TaskDto taskDto = new TaskDto();
        Task createdTask = new Task();
        when(taskService.create(taskDto)).thenReturn(createdTask);

        ResponseEntity<Task> response = taskController.createTask(taskDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdTask, response.getBody());
        verify(taskService).create(taskDto);
    }

    @Test
    @DisplayName("Should fail to create task when user is not admin")
    void createTask_ShouldThrowAccessDenied_WhenUserIsNotAdmin() {
        TaskDto taskDto = new TaskDto();
        doThrow(new AccessDeniedException("Access Denied")).when(taskService).create(any(TaskDto.class));

        assertThrows(AccessDeniedException.class, () -> taskController.createTask(taskDto));
    }

    @Test
    @DisplayName("Should update task successfully by authorized user")
    void updateTask_ShouldReturnUpdatedTask_WhenUserIsAuthorized() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        TaskDto updatedTask = new TaskDto();
        when(taskService.update(taskDto)).thenReturn(updatedTask);

        ResponseEntity<TaskDto> response = taskController.updateTask(taskId, taskDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedTask, response.getBody());
        verify(taskService).update(taskDto);
    }

    @Test
    @DisplayName("Should fail to update task when user is not authorized")
    void updateTask_ShouldThrowAccessDenied_WhenUserIsNotAuthorized() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        doThrow(new AccessDeniedException("Access Denied")).when(taskService).update(any(TaskDto.class));

        assertThrows(AccessDeniedException.class, () -> taskController.updateTask(taskId, taskDto));
    }

    @Test
    @DisplayName("Should retrieve a task successfully by authorized user")
    void getTaskById_ShouldReturnTask_WhenUserIsAuthorized() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        when(taskService.findById(taskId)).thenReturn(Optional.of(taskDto));

        ResponseEntity<TaskDto> response = taskController.getTaskById(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskDto, response.getBody());
        verify(taskService).findById(taskId);
    }

    @Test
    @DisplayName("Should fail to retrieve task when user is not authorized")
    void getTaskById_ShouldThrowAccessDenied_WhenUserIsNotAuthorized() {
        Long taskId = 1L;
        doThrow(new AccessDeniedException("Access Denied")).when(taskService).findById(anyLong());

        assertThrows(AccessDeniedException.class, () -> taskController.getTaskById(taskId));
    }

    @Test
    @DisplayName("Should delete a task successfully by admin")
    void deleteTask_ShouldReturnNoContent_WhenTaskIsDeleted() {
        Long taskId = 1L;
        when(taskService.delete(taskId)).thenReturn(true);

        ResponseEntity<Void> response = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(taskService).delete(taskId);
    }

    @Test
    @DisplayName("Should fail to delete task when user is not admin")
    void deleteTask_ShouldThrowAccessDenied_WhenUserIsNotAdmin() {
        Long taskId = 1L;
        doThrow(new AccessDeniedException("Access Denied")).when(taskService).delete(anyLong());

        assertThrows(AccessDeniedException.class, () -> taskController.deleteTask(taskId));
    }

    @Test
    @DisplayName("Should assign a task successfully by admin")
    void assignTask_ShouldReturnUpdatedTask_WhenUserIsAdmin() {
        Long taskId = 1L;
        String assigneeEmail = "test@example.com";
        TaskDto updatedTask = new TaskDto();
        when(taskService.assignTask(taskId, assigneeEmail)).thenReturn(updatedTask);

        ResponseEntity<TaskDto> response = taskController.assignTask(taskId, assigneeEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedTask, response.getBody());
        verify(taskService).assignTask(taskId, assigneeEmail);
    }

    @Test
    @DisplayName("Should fail to assign task when user is not admin")
    void assignTask_ShouldThrowAccessDenied_WhenUserIsNotAdmin() {
        Long taskId = 1L;
        String assigneeEmail = "test@example.com";
        doThrow(new AccessDeniedException("Access Denied")).when(taskService).assignTask(anyLong(), anyString());

        assertThrows(AccessDeniedException.class, () -> taskController.assignTask(taskId, assigneeEmail));
    }

    @Test
    @DisplayName("Should retrieve tasks as admin")
    void getTasks_ShouldReturnTasks_WhenUserIsAdmin() {
        Pageable pageable = Pageable.ofSize(10);
        String filter = "test";
        Page<TaskDto> tasksPage = mock(Page.class);
        when(taskService.findAll(pageable, filter)).thenReturn(tasksPage);

        ResponseEntity<Page<TaskDto>> response = taskController.getTasks(pageable, filter);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tasksPage, response.getBody());
        verify(taskService).findAll(pageable, filter);
    }
}
