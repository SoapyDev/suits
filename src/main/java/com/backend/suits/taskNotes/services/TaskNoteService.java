package com.backend.suits.taskNotes.services;

import com.backend.suits.taskNotes.models.TaskNote;
import com.backend.suits.taskNotes.repositories.TaskNoteRepository;
import com.backend.suits.tasks.models.Task;
import com.backend.suits.tasks.services.TaskService;
import com.backend.suits.userProfile.models.UserProfile;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class TaskNoteService {

    private final TaskNoteRepository taskNoteRepository;
    private final TaskService taskService;

    @Autowired
    public TaskNoteService(TaskNoteRepository taskNoteRepository, TaskService taskService) {
        this.taskNoteRepository = taskNoteRepository;
        this.taskService = taskService;
    }

    public TaskNote createTaskNote(Long taskId, String content, UserProfile user){
        Task task = taskService.getTaskById(taskId);
        if (task == null) {
            throw new RuntimeException("Tâche introuvable avec l'ID : " + taskId);
        }

        TaskNote taskNote = new TaskNote(
                user,
                LocalDate.now(),
                content,
                taskService.getTaskById(taskId)
        );

        return taskNoteRepository.save(taskNote);
    }

    public TaskNote updateTaskNote(Long taskNoteId, String content){
        TaskNote taskNote = taskNoteRepository.findById(taskNoteId)
                .orElseThrow(() -> new EntityNotFoundException("La note avec l'id " + taskNoteId + " est introuvable."));

        taskNote.setContent(content);
        taskNote.setUpdateDate(LocalDate.now());
        return taskNoteRepository.save(taskNote);
    }

    public boolean deleteTaskNote(Long taskNoteId){

        if(taskNoteRepository.existsById(taskNoteId)){
            taskNoteRepository.deleteById(taskNoteId);
            return true;
        } else{
            return false;
        }
    }

    public TaskNote findTaskNoteById(Long taskNoteId){
        return taskNoteRepository.findTaskNoteById(taskNoteId);
    }

}
