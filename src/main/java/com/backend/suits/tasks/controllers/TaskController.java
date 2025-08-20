package com.backend.suits.tasks.controllers;

import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.legalFile.services.LegalFileService;
import com.backend.suits.tasks.models.Task;
import com.backend.suits.tasks.services.TaskService;
import com.backend.suits.userProfile.models.UserProfile;
import com.backend.suits.userProfile.services.UserProfileService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class TaskController {

    private final TaskService taskService;
    private final UserProfileService userProfileService;
    private final LegalFileService legalFileService;


    public TaskController(TaskService taskService, UserProfileService userProfileService, LegalFileService legalFileService) {
        this.taskService = taskService;
        this.userProfileService = userProfileService;
        this.legalFileService = legalFileService;
    }

    @PostMapping("/api/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {

        Task createdTask = taskService.createTask(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/api/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @Valid @RequestBody Task task) {

        Task updatedTask = taskService.updateTask(taskId, task);

        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/api/tasks/{taskId}")
    public ResponseEntity<?> patchTask(@PathVariable Long taskId,
                                          @Valid @RequestBody Map<String, Object> updatedFields,
                                          Authentication authentication) {

        UserProfile user = userProfileService.findByUsername(authentication.getName());
        Task task = taskService.getTaskById(taskId);

        LegalFile legalFile = legalFileService.findById(task.getLegalFileId());

        if(!legalFile.getLegalFileTeamMembers().contains(user)) {
            return ResponseEntity.status(403).body("Vous n'avez pas les autorisations pour modifier cette tâche.");
        }

        Task updatedTask = taskService.patchTask(taskId, updatedFields, user);

        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    @GetMapping("/api/tasks/{taskId}")
    public ResponseEntity<Task> getTaskFromId(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);

        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }


    @GetMapping("/api/legalFile/{legalFileId}/tasks")
    public ResponseEntity<List<Task>> getTasksFromLegalFileId(@PathVariable String legalFileId) {
        List<Task> tasks = taskService.getTaskByLegalFileId(legalFileId);

        return tasks.isEmpty() ? ResponseEntity.status(204).build(): ResponseEntity.ok(tasks);
    }

    @GetMapping("/api/user/tasks")
    public ResponseEntity<List<Task>> getAllTasksFromUserId(Authentication authentication) {

        String username = authentication.getName();
        Optional<UserProfile> optionalUser = Optional.ofNullable(userProfileService.findByUsername(username));

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserProfile user = optionalUser.get();

        List<Task> tasks = taskService.getAllTasksByAssignee(user);
        return tasks !=null ? ResponseEntity.ok(tasks) : ResponseEntity.status(204).build();
    }


}
