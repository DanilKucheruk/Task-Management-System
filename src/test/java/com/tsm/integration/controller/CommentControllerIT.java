package com.tsm.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsm.dto.CommentDto;
import com.tsm.dto.UserDto;
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

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@AutoConfigureMockMvc
public class CommentControllerIT extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("Should add a new comment to a task successfully")
    void addComment_ShouldReturnCreatedComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("This is a new comment");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks/2/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", is("This is a new comment")));
    }

    @Test
    @DisplayName("Should retrieve all comments for a task")
    void getComments_ShouldReturnComments_ForExistingTask() throws Exception {
        Long taskId = 3L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/" + taskId + "/comments"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("This is a comment for Task 3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value("Another comment for Task 3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].content").value("Feedback on Task 3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author.id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].author.id").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].author.id").value(5L));
    }


    @Test
    @DisplayName("Should update a comment successfully")
    void updateComment_ShouldReturnUpdatedComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("Updated comment content");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/3/comments/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Updated comment content")));
    }


    @Test
    @DisplayName("Should delete a comment successfully")
    void deleteComment_ShouldReturnNoContent_WhenCommentExists() throws Exception {
        Long commentId = 2L; 

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/" + 3L + "/comments/" + commentId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return Not Found when comment to delete does not exist")
    void deleteComment_ShouldReturnNotFound_WhenCommentDoesNotExist() throws Exception {
        Long nonExistentCommentId = 99L; 

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/" + 3L + "/comments/" + nonExistentCommentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return Bad Request when adding a comment with missing content")
    void addComment_ShouldReturnBadRequest_WhenContentIsMissing() throws Exception {
        Long taskId = 2L; 

        UserDto author = new UserDto(2L, "test@gmail.com", "test");
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthor(author);
        commentDto.setCreatedAt(LocalDateTime.now());

        String jsonContent = objectMapper.writeValueAsString(commentDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks/" + taskId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return Bad Request when updating a comment with missing content")
    void updateComment_ShouldReturnBadRequest_WhenContentIsMissing() throws Exception {
        Long commentId = 2L; 

        UserDto author = new UserDto(2L, "test@gmail.com", "test");
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthor(author);
        commentDto.setCreatedAt(LocalDateTime.now());

        String jsonContent = objectMapper.writeValueAsString(commentDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/" + 3L + "/comments/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());
    }


}
