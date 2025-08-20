package com.backend.suits.tasks.controllers;

import com.backend.suits.entity.Role;
import com.backend.suits.entity.User;
import com.backend.suits.tasks.models.Status;
import com.backend.suits.tasks.models.Task;
import com.backend.suits.tasks.services.TaskService;
import com.backend.suits.userProfile.models.UserProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private Task task;
    private UserProfile assignee;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        assignee = new UserProfile("username@gmail.com","Paul Bédard");
        task = Task.builder()
                .id(1L)
                .legalFileId("LegalFile Id")
                .title("Task Title")
                .description("Task Description")
                .assignee(assignee)
                .status(Status.fromString("En cours"))
                .priority(1)
                .startDate(LocalDate.parse("2025-03-10"))
                .dueDate(LocalDate.parse("2025-04-15"))
                .build();
    }

    @Test
    void createTaskSuccess() throws Exception {

        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(task.getTitle()));
    }
//    @Test
//    void createTaskTitleNull() throws Exception {
//
//        task.setTitle(null);
//
//        mockMvc.perform(post("/api/tasks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(task)))
//                .andExpect(status().isBadRequest());
//
//
//    }

    @Test
    void createTaskWrongPriority() throws Exception {

        task.setPriority(8);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest());

    }

//    @Test
//    void createTaskWrongDueDate() throws Exception {
//        task.setStartDate(LocalDate.parse("2025-05-10"));
//        task.setDueDate(LocalDate.parse("2025-05-02"));
//
//        mockMvc.perform(post("/api/tasks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(task)))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    void updateTaskSuccess() throws Exception {

        task.setTitle("Title Changed");

        when(taskService.updateTask(any(Long.class),any(Task.class))).thenReturn(task);

        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value("Title Changed"));

    }

    @Test
    void updateTaskNotFound() throws Exception {

        task.setTitle("Title Changed");

        when(taskService.updateTask(any(Long.class),any(Task.class))).thenReturn(null);

        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                        .andExpect(status().isNotFound());

    }

    @Test
    void updateTaskWrongFormat() throws Exception {

        task.setPriority(8);

        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getAllTasksFromLegalFileIdSuccess() throws Exception {
        Task task2 = Task.builder()
                .id(2L)
                .legalFileId("LegalFile Id")
                .title("Task Title2")
                .description("Task Description2")
                .assignee(assignee)
                .status(Status.fromString("En cours"))
                .priority(2)
                .startDate(LocalDate.parse("2025-03-12"))
                .dueDate(LocalDate.parse("2025-04-22"))
                .build();

        List<Task> tasks = List.of(task, task2);

        when(taskService.getTaskByLegalFileId(any(String.class))).thenReturn(tasks);


        mockMvc.perform(get("/api/legalFile/LegalFile Id/tasks"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.size()").value(tasks.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task Title"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task Title2"));
    }

    @Test
    void getAllTasksFromLegalFileIdNotFound() throws Exception {

        when(taskService.getTaskByLegalFileId(any(String.class))).thenReturn(Collections.emptyList());


        mockMvc.perform(get("/api/legalFile/LegalFile Id/tasks"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTaskFromIdSuccess() throws Exception {

        task.setId(12L);

        when(taskService.getTaskById(any(Long.class))).thenReturn(task);

        mockMvc.perform(get("/api/tasks/12"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(task.getId()));
    }

    @Test
    void getTaskFromIdNotfound() throws Exception {

        task.setId(12L);

        when(taskService.getTaskById(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/api/tasks/22"))
                .andExpect(status().isNotFound());
    }

}