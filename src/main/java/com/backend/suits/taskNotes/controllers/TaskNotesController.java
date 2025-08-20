package com.backend.suits.taskNotes.controllers;

import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.legalFile.services.LegalFileService;
import com.backend.suits.taskNotes.models.TaskNote;
import com.backend.suits.taskNotes.models.TaskNoteRequest;
import com.backend.suits.taskNotes.services.TaskNoteService;
import com.backend.suits.tasks.models.Task;
import com.backend.suits.tasks.services.TaskService;
import com.backend.suits.userProfile.models.UserProfile;
import com.backend.suits.userProfile.services.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskNotesController {

    private final TaskNoteService taskNoteService;
    private final TaskService taskService;
    private final UserProfileService userProfileService;
    private final LegalFileService legalFileService;

    public TaskNotesController(TaskNoteService taskNoteService, TaskService taskService, UserProfileService userProfileService, LegalFileService legalFileService) {
        this.taskNoteService = taskNoteService;
        this.taskService = taskService;
        this.userProfileService = userProfileService;
        this.legalFileService = legalFileService;
    }

    @PostMapping("/api/tasks/{taskId}/notes")
    public ResponseEntity<?> createTaskNote(@PathVariable Long taskId,
                                            @Valid @RequestBody TaskNoteRequest request,
                                            Authentication authentication) {

        System.out.println("*** Creating task note");
        System.out.println("*** taskId: " + taskId);
        System.out.println("*** request: " + request);
        UserProfile user = userProfileService.findByUsername(authentication.getName());
        Task task = taskService.getTaskById(taskId);

        LegalFile legalFile = legalFileService.findById(task.getLegalFileId());

        if(!legalFile.getLegalFileTeamMembers().contains(user)) {
            return ResponseEntity.status(403).body("Vous n'avez pas les autorisations pour ajouter une note.");
        }

        TaskNote newTaskNote = taskNoteService.createTaskNote(taskId, request.getContent(), user);

        return  ResponseEntity.ok(newTaskNote);
    }

    @PutMapping("/api/tasks/{taskId}/notes/{noteId}")
    public ResponseEntity<?> updateTaskNote(@PathVariable("taskId") Long taskId,
                                            @PathVariable("noteId") Long taskNoteId,
                                            @Valid @RequestBody TaskNoteRequest request,
                                            Authentication authentication) {

        UserProfile user = userProfileService.findByUsername(authentication.getName());
        TaskNote taskNote = taskNoteService.findTaskNoteById(taskNoteId);

        if(!taskNote.getCreator().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(403).body("Vous n'avez pas les autorisations pour ajouter une note.");
        }

        TaskNote updatedTaskNote = taskNoteService.updateTaskNote(taskNoteId, request.getContent());

        return  ResponseEntity.ok(updatedTaskNote);
    }

    @DeleteMapping("/api/tasks/{taskId}/notes/{noteId}")
    public ResponseEntity<?> deleteTaskNote(@PathVariable("taskId") Long taskId,
                                            @PathVariable("noteId") Long taskNoteId,
                                            Authentication authentication) {

        UserProfile user = userProfileService.findByUsername(authentication.getName());
        TaskNote taskNote = taskNoteService.findTaskNoteById(taskNoteId);

        if(!taskNote.getCreator().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(403).body("Vous n'avez pas les autorisations pour effacer cette note.");
        }

        if( taskNoteService.deleteTaskNote(taskNoteId)){
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.notFound().build();

    }

}
