package com.backend.suits.tasks.services;

import com.backend.suits.tasks.models.Task;
import com.backend.suits.tasks.repositories.TaskRepository;
import com.backend.suits.userProfile.models.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void getTaskByIdSuccess() {

        task.setId(1L);
        task.setTitle("Title");
        task.setDescription("Description");
        task.setLegalFileId("TEST-test");
        task.setStartDate(LocalDate.parse("2025-03-10"));
        task.setDueDate(LocalDate.parse("2025-04-15"));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);

        assertNotNull(foundTask);
        assertEquals(task.getId(), foundTask.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskByIdNotFound() {

        task.setId(1L);
        task.setTitle("Title");
        task.setDescription("Description");
        task.setLegalFileId("TEST-test");
        task.setStartDate(LocalDate.parse("2025-03-10"));
        task.setDueDate(LocalDate.parse("2025-04-15"));

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Task foundTask = taskService.getTaskById(1L);

        assertNull(foundTask);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskByLegalFileIdSuccess() {

        String legalFileId = "TEST-test";
        Task task1 = new Task();
        Task task2 = new Task();

        task1.setId(1L);
        task1.setTitle("Title1");
        task1.setDescription("Description1");
        task1.setLegalFileId(legalFileId);
        task1.setStartDate(LocalDate.parse("2025-03-11"));
        task1.setDueDate(LocalDate.parse("2025-04-11"));

        task2.setId(2L);
        task2.setTitle("Title2");
        task2.setDescription("Description2");
        task2.setLegalFileId(legalFileId);
        task2.setStartDate(LocalDate.parse("2025-03-12"));
        task2.setDueDate(LocalDate.parse("2025-04-12"));

        List<Task> taskList = Arrays.asList( task1,task2);

        when(taskRepository.findByLegalFileId(legalFileId)).thenReturn(taskList);

        List<Task> foundTasks = taskService.getTaskByLegalFileId(legalFileId);

        assertNotNull(foundTasks);
        assertEquals(2, foundTasks.size());
        assertEquals(task1.getId(), foundTasks.get(0).getId());
        assertEquals(task2.getId(), foundTasks.get(1).getId());
        verify(taskRepository, times(1)).findByLegalFileId(legalFileId);

    }

    @Test
    void getTaskByLegalFileIdNotFound() {

        String legalFileId = "TEST-test";

        when(taskRepository.findByLegalFileId(legalFileId)).thenReturn(Collections.emptyList());

        List<Task> foundTasks = taskService.getTaskByLegalFileId(legalFileId);

        assertNotNull(foundTasks);
        assertEquals(0, foundTasks.size());
        verify(taskRepository, times(1)).findByLegalFileId(legalFileId);

    }

    @Test
    void getAllTasksByAssigneeSuccess() {

        UserProfile assignee = new UserProfile("username@gmail.com","Paul");

        String legalFileId = "TEST-test";
        Task task1 = new Task();
        Task task2 = new Task();

        task1.setId(1L);
        task1.setTitle("Title1");
        task1.setDescription("Description1");
        task1.setLegalFileId(legalFileId);
        task1.setAssignee(assignee);
        task1.setStartDate(LocalDate.parse("2025-03-11"));
        task1.setDueDate(LocalDate.parse("2025-04-11"));

        task2.setId(2L);
        task2.setTitle("Title2");
        task2.setDescription("Description2");
        task2.setLegalFileId(legalFileId);
        task2.setAssignee(assignee);
        task2.setStartDate(LocalDate.parse("2025-03-12"));
        task2.setDueDate(LocalDate.parse("2025-04-12"));

        List<Task> taskList = Arrays.asList( task1,task2);

        when(taskRepository.findByAssignee(any(UserProfile.class))).thenReturn(taskList);

        List<Task> foundTasks = taskService.getAllTasksByAssignee(assignee);

        assertNotNull(foundTasks);
        assertEquals(2, foundTasks.size());
        assertEquals(task1.getId(), foundTasks.get(0).getId());
        assertEquals(task2.getId(), foundTasks.get(1).getId());
        verify(taskRepository, times(1)).findByAssignee(assignee);

    }

    @Test
    void getAllTasksByAssigneeNotFound() {

        UserProfile assignee = new UserProfile("username@gmail.com","Paul");

        when(taskRepository.findByAssignee(any(UserProfile.class))).thenReturn(Collections.emptyList());

        List<Task> foundTasks = taskService.getAllTasksByAssignee(assignee);

        assertNotNull(foundTasks);
        assertEquals(0, foundTasks.size());
        verify(taskRepository, times(1)).findByAssignee(assignee);

    }

    @Test
    void testCreateTaskSuccess() {
        task.setTitle("Title");
        task.setDescription("Description");
        task.setLegalFileId("TEST-test");
        task.setStartDate(LocalDate.parse("2025-03-10"));
        task.setDueDate(LocalDate.parse("2025-04-15"));

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task savedTask = taskService.createTask(task);

        assertNotNull(savedTask);
        assertEquals(task.getTitle(), savedTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }


    @Test
    void updateTask() {

        task.setId(1L);
        task.setTitle("Title");
        task.setDescription("Description");
        task.setLegalFileId("TEST-test");
        task.setStartDate(LocalDate.parse("2025-03-10"));
        task.setDueDate(LocalDate.parse("2025-04-15"));

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Title");
        updatedTask.setDescription("Description Updated");
        updatedTask.setLegalFileId("TEST-test");
        updatedTask.setStartDate(LocalDate.parse("2025-03-10"));
        updatedTask.setDueDate(LocalDate.parse("2025-04-15"));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertNotNull(result);
        assertEquals(updatedTask.getId(), result.getId());
        assertEquals(updatedTask.getTitle(), result.getTitle());
        assertEquals(updatedTask.getDescription(), result.getDescription());
        assertEquals(updatedTask.getLegalFileId(), result.getLegalFileId());
        assertEquals(updatedTask.getStartDate(), result.getStartDate());
        assertEquals(updatedTask.getDueDate(), result.getDueDate());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(updatedTask);

    }
    @Test
    void updateTaskNotFound() {

        task.setId(1L);
        task.setTitle("Title");
        task.setDescription("Description");
        task.setLegalFileId("TEST-test");
        task.setStartDate(LocalDate.parse("2025-03-10"));
        task.setDueDate(LocalDate.parse("2025-04-15"));

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Title");
        updatedTask.setDescription("Description Updated");
        updatedTask.setLegalFileId("TEST-test");
        updatedTask.setStartDate(LocalDate.parse("2025-03-10"));
        updatedTask.setDueDate(LocalDate.parse("2025-04-15"));

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(1L, updatedTask);
        });

        assertEquals("La tâche à mettre à jour n'est pas trouvée.", exception.getMessage());

    }

    @Test
    void deleteTaskSuccess() {
        task.setId(1L);
        task.setTitle("Title");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).delete(task);

    }

    @Test
    void deleteTaskNotFound() {
        task.setId(1L);
        task.setTitle("Title");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
                    taskService.deleteTask(1L);
                });

        assertEquals("La tâche à supprimer n'existe pas.", exception.getMessage());

    }
}