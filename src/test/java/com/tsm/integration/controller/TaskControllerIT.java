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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@IT
@AutoConfigureMockMvc
public class TaskControllerIT extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Admin can create a new task")
    @WithMockUser(username = "test@gmail.com", roles = "ADMIN")
    void adminCreateTask_ShouldReturnCreatedTask() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("New Task");
        taskDto.setDescription("Description for new task");
        taskDto.setStatus(Status.PENDING);
        taskDto.setPriority(Priority.HIGH);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Task")))
                .andExpect(jsonPath("$.description", is("Description for new task")));
    }

    @Test
    @DisplayName("User cannot create a new task")
    @WithMockUser(username = "user", roles = {"USER"})
    void userCreateTask_ShouldReturnForbidden() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("User Task");
        taskDto.setDescription("User cannot create this");
        taskDto.setStatus(Status.PENDING);
        taskDto.setPriority(Priority.MEDIUM);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin can update a task")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminUpdateTask_ShouldReturnUpdatedTask() throws Exception {
        Long taskId = 2L;
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Updated Task");
        taskDto.setDescription("Updated Description");
        taskDto.setStatus(Status.COMPLETED);
        taskDto.setPriority(Priority.LOW);

        mockMvc.perform(put("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Task")))
                .andExpect(jsonPath("$.description", is("Updated Description")));
    }

    @Test
    @DisplayName("User with access can update their task")
    @WithMockUser(username = "user3", roles = {"USER"})
    void userUpdateTask_ShouldReturnUpdatedTask() throws Exception {
        Long taskId = 3L;
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("User Updated Task");
        taskDto.setDescription("User Updated Description");
        taskDto.setStatus(Status.IN_PROGRESS);
        taskDto.setPriority(Priority.MEDIUM);

        mockMvc.perform(put("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("User Updated Task")))
                .andExpect(jsonPath("$.description", is("User Updated Description")));
    }

    @Test
    @DisplayName("Admin can delete a task")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminDeleteTask_ShouldReturnNoContent() throws Exception {
        Long taskId = 3L;

        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("User cannot delete a task")
    @WithMockUser(username = "user", roles = {"USER"})
    void userDeleteTask_ShouldReturnForbidden() throws Exception {
        Long taskId = 3L;

        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin can assign a task")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminAssignTask_ShouldReturnUpdatedTask() throws Exception {
        Long taskId = 2L;
        String assigneeEmail = "userAsign@gmail.com";

        mockMvc.perform(patch("/api/tasks/" + taskId + "/assign")
                .param("assigneeEmail", assigneeEmail))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User cannot assign a task")
    @WithMockUser(username = "user", roles = {"USER"})
    void userAssignTask_ShouldReturnForbidden() throws Exception {
        Long taskId = 2L;
        String assigneeEmail = "userAsign@gmail.com";

        mockMvc.perform(patch("/api/tasks/" + taskId + "/assign")
                .param("assigneeEmail", assigneeEmail))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin can view all tasks")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminGetTasks_ShouldReturnTaskList() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User cannot view all tasks")
    @WithMockUser(username = "user", roles = {"USER"})
    void userGetTasks_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin can retrieve task by ID")
    @WithMockUser(username = "test@gmail.com", roles = {"ADMIN"})
    void adminCanRetrieveTaskById() throws Exception {
        Long existingTaskId = 2L;

        mockMvc.perform(get("/api/tasks/" + existingTaskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 2"))
                .andExpect(jsonPath("$.description").value("Description for Task 2"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    @DisplayName("User with access can retrieve task by ID")
    @WithMockUser(username = "user3", roles = {"USER"})
    void userWithAccessCanRetrieveTaskById() throws Exception {
        Long taskIdWithAccess = 3L; // Assuming user3 has access to task 3

        mockMvc.perform(get("/api/tasks/" + taskIdWithAccess))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 3"))
                .andExpect(jsonPath("$.description").value("Description for Task 3"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
    }

    @Test
    @DisplayName("User without access cannot retrieve task by ID")
    @WithMockUser(username = "user3", roles = {"USER"})
    void userWithoutAccessCannotRetrieveTaskById() throws Exception {
        Long taskIdWithoutAccess = 2L; // Assuming user3 does not have access to task 2

        mockMvc.perform(get("/api/tasks/" + taskIdWithoutAccess))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("User with access can change task status")
    @WithMockUser(username = "user2", roles = {"USER"})
    void userWithAccessCanChangeTaskStatus() throws Exception {
        Long taskIdWithAccess = 2L; // Assuming user2 has access to task 2
        Status newStatus = Status.COMPLETED;

        mockMvc.perform(patch("/api/tasks/" + taskIdWithAccess + "/status")
                .param("status", newStatus.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(newStatus.name()));
    }

    @Test
    @DisplayName("User without access cannot change task status")
    @WithMockUser(username = "user2", roles = {"USER"})
    void userWithoutAccessCannotChangeTaskStatus() throws Exception {
        Long taskIdWithoutAccess = 3L; // Assuming user3 does not have access to task 3
        Status newStatus = Status.PENDING;

        mockMvc.perform(patch("/api/tasks/" + taskIdWithoutAccess + "/status")
                .param("status", newStatus.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin can change task status")
    @WithMockUser(username = "test@gmail.com", roles = {"ADMIN"})
    void adminCanChangeTaskStatus() throws Exception {
        Long existingTaskId = 2L;
        Status newStatus = Status.IN_PROGRESS;

        mockMvc.perform(patch("/api/tasks/" + existingTaskId + "/status")
                .param("status", newStatus.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(newStatus.name()));
    }
}
