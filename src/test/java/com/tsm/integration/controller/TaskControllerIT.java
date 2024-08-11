package com.tsm.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsm.dto.TaskDto;
import com.tsm.entity.Priority;
import com.tsm.entity.Status;
import com.tsm.integration.IntegrationTestBase;
import com.tsm.integration.annotation.IT;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@AutoConfigureMockMvc
public class TaskControllerIT extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create a new task successfully")
    void createTask_ShouldReturnCreatedTask() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("New Task");
        taskDto.setDescription("Description for new task");
        taskDto.setStatus(Status.PENDING);
        taskDto.setPriority(Priority.HIGH);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Task")))
                .andExpect(jsonPath("$.description", is("Description for new task")));
    }

    @Test
    @DisplayName("Should return Not Found when task does not exist")
    void getTaskById_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        Long nonExistentTaskId = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/" + nonExistentTaskId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return task details when task exists")
    void getTaskById_ShouldReturnTask_WhenTaskExists() throws Exception {
        Long existingTaskId = 2L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/" + existingTaskId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Task 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description for Task 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("PENDING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("HIGH"));
    }

    @Test
    @DisplayName("Should delete a task successfully")
    void deleteTask_ShouldReturnNoContent_WhenTaskIsDeleted() throws Exception {
        Long existingTaskId = 3L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/" + existingTaskId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return Not Found when task to delete does not exist")
    void deleteTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        Long nonExistentTaskId = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/" + nonExistentTaskId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should change task status successfully")
    void changeTaskStatus_ShouldReturnUpdatedTask() throws Exception {
        Long existingTaskId = 2L;
        Status newStatus = Status.COMPLETED;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/tasks/" + existingTaskId + "/status")
                .param("status", newStatus.name()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(newStatus.name()));
    }

    @Test
    @DisplayName("Should assign a task successfully")
    void assignTask_ShouldReturnUpdatedTask() throws Exception {
        Long existingTaskId = 2L;
        String assigneeEmail = "userAsign@gmail.com";

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/tasks/" + existingTaskId + "/assign")
                .param("assigneeEmail", assigneeEmail))
                .andExpect(status().isOk());
    }

}