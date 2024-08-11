package com.tsm.unit.controller;

import com.tsm.controller.TaskController;
import com.tsm.dto.TaskDto;
import com.tsm.entity.Status;
import com.tsm.entity.Task;
import com.tsm.service.TaskService;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    @DisplayName("Should create a new task successfully")
    void createTask_ShouldReturnCreatedTask() {
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
    @DisplayName("Should return Not Found when task does not exist")
    void getTaskById_ShouldReturnNotFound_WhenTaskDoesNotExist() {
        Long taskId = 1L;
        when(taskService.findById(taskId)).thenReturn(Optional.empty());

        ResponseEntity<TaskDto> response = taskController.getTaskById(taskId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(taskService).findById(taskId);
    }

    @Test
    @DisplayName("Should return task details when task exists")
    void getTaskById_ShouldReturnTask_WhenTaskExists() {
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
    @DisplayName("Should delete a task successfully")
    void deleteTask_ShouldReturnNoContent_WhenTaskIsDeleted() {
        Long taskId = 1L;
        when(taskService.delete(taskId)).thenReturn(true);

        ResponseEntity<Void> response = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(taskService).delete(taskId);
    }

    @Test
    @DisplayName("Should return Not Found when task to delete does not exist")
    void deleteTask_ShouldReturnNotFound_WhenTaskDoesNotExist() {
        Long taskId = 1L;
        when(taskService.delete(taskId)).thenReturn(false);

        ResponseEntity<Void> response = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(taskService).delete(taskId);
    }

    @Test
    @DisplayName("Should change task status successfully")
    void changeTaskStatus_ShouldReturnUpdatedTask() {
        Long taskId = 1L;
        Status status = Status.COMPLETED;
        TaskDto updatedTaskDto = new TaskDto();
        when(taskService.changeStatus(taskId, status)).thenReturn(updatedTaskDto);

        ResponseEntity<TaskDto> response = taskController.changeTaskStatus(taskId, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedTaskDto, response.getBody());
        verify(taskService).changeStatus(taskId, status);
    }

    @Test
    @DisplayName("Should assign a task successfully")
    void assignTask_ShouldReturnUpdatedTask() {
        Long taskId = 1L;
        String assigneeEmail = "test@example.com" ;
        TaskDto updatedTaskDto = new TaskDto();
        when(taskService.assignTask(taskId, assigneeEmail)).thenReturn(updatedTaskDto);

        ResponseEntity<TaskDto> response = taskController.assignTask(taskId, assigneeEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedTaskDto, response.getBody());
        verify(taskService).assignTask(taskId, assigneeEmail);
    }

    @Test
    @DisplayName("Should retrieve a page of tasks with filter")
    void getTasks_ShouldReturnPageOfTasks() {
        Pageable pageable = Pageable.ofSize(10);
        String filter = "test";
        Page<TaskDto> taskPage = mock(Page.class);
        when(taskService.findAll(pageable, filter)).thenReturn(taskPage);

        ResponseEntity<Page<TaskDto>> response = taskController.getTasks(pageable, filter);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskPage, response.getBody());
        verify(taskService).findAll(pageable, filter);
    }

    @Test
    @DisplayName("Should retrieve a page of tasks without filter")
    void getTasks_ShouldReturnPageOfTasksWithoutFilter() {
        Pageable pageable = Pageable.ofSize(10);
        Page<TaskDto> taskPage = mock(Page.class);
        when(taskService.findAll(pageable, null)).thenReturn(taskPage);

        ResponseEntity<Page<TaskDto>> response = taskController.getTasks(pageable, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskPage, response.getBody());
        verify(taskService).findAll(pageable, null);
    }
}