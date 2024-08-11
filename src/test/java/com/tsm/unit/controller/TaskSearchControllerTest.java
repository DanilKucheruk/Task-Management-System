package com.tsm.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsm.controller.TaskSearchController;
import com.tsm.dto.TaskDto;
import com.tsm.service.TaskSearchService;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class TaskSearchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskSearchService taskSearchService;

    @InjectMocks
    private TaskSearchController taskSearchController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskSearchController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(viewResolver())
                .build();
    }

    @Test
    void testFindByAuthor() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Sample Task");
        Page<TaskDto> page = new PageImpl<>(Collections.singletonList(taskDto), PageRequest.of(0, 10), 1);
    
        when(taskSearchService.findByAuthor(anyLong(), any(Pageable.class))).thenReturn(page);
    
        mockMvc.perform(get("/api/tasks/search/by-author")
                .param("authorId", "1")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(new PageImpl<>(Collections.singletonList(taskDto), PageRequest.of(0, 10), 1))));
    }

    @Test
    void testFindByAssignee() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Sample Task");
        Page<TaskDto> page = new PageImpl<>(Collections.singletonList(taskDto), PageRequest.of(0, 10), 1);

        when(taskSearchService.findByAssignee(anyLong(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tasks/search/by-assignee")
                .param("assigneeId", "1")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(page)));
    }

    @Test
    void testFindByStatus() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Sample Task");
        Page<TaskDto> page = new PageImpl<>(Collections.singletonList(taskDto), PageRequest.of(0, 10), 1);

        when(taskSearchService.findByStatus(anyString(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tasks/search/by-status")
                .param("status", "PENDING")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(page)));
    }

    @Test
    void testFindByPriority() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Sample Task");
        Page<TaskDto> page = new PageImpl<>(Collections.singletonList(taskDto), PageRequest.of(0, 10), 1);

        when(taskSearchService.findByPriority(anyString(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tasks/search/by-priority")
                .param("priority", "HIGH")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(page)));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}
