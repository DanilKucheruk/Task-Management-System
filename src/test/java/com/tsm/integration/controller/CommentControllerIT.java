package com.tsm.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsm.dto.CommentDto;
import com.tsm.integration.IntegrationTestBase;
import com.tsm.integration.annotation.IT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@IT
@AutoConfigureMockMvc
public class CommentControllerIT extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
   
    @Test
    @WithMockUser(username = "test@gmail.com", roles = {"ADMIN"})
    @DisplayName("Add a comment as ADMIN user should return the created comment")
    public void addComment_AsAdmin_ShouldReturnCreatedComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("New comment for Task 3");

        mockMvc.perform(post("/api/tasks/3/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("New comment for Task 3"));
    }

    @Test
    @WithMockUser(username = "user2", roles = {"USER"})
    public void addComment_AsUser_ShouldReturnCreatedComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("Comment from user2 for Task 2");

        mockMvc.perform(post("/api/tasks/2/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Comment from user2 for Task 2"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getComments_AsAdmin_ShouldReturnComments() throws Exception {
        mockMvc.perform(get("/api/tasks/3/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].content").value("This is a comment for Task 3"))
                .andExpect(jsonPath("$[1].content").value("Another comment for Task 3"))
                .andExpect(jsonPath("$[2].content").value("Feedback on Task 3"));
    }

    @Test
    @WithMockUser(username = "user2", roles = {"USER"})
    public void getComments_AsUserWithAccess_ShouldReturnComments() throws Exception {
        mockMvc.perform(get("/api/tasks/2/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].content").value("Comment for Task 2"));
    }

    @Test
    @WithMockUser(username = "user4", roles = {"USER"})
    @DisplayName("Retrieve comments as USER without access to the task should return Forbidden")
    public void getComments_AsUserWithoutAccess_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/tasks/2/comments"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update a comment as ADMIN should return the updated comment")
    public void updateComment_AsAdmin_ShouldReturnUpdatedComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("Updated comment content");

        mockMvc.perform(put("/api/tasks/3/comments/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated comment content"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete a comment as ADMIN should return No Content")
    public void deleteComment_AsAdmin_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/tasks/3/comments/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user2", roles = {"USER"})
    @DisplayName("Delete a comment as USER should return Forbidden")
    public void deleteComment_AsUser_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/tasks/3/comments/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete a non-existing comment should return Not Found")
    public void deleteComment_NonExistingComment_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/tasks/3/comments/999"))
                .andExpect(status().isNotFound());
    }

}
