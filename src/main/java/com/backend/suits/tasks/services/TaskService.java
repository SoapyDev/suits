package com.backend.suits.tasks.services;

import com.backend.suits.tasks.models.Status;
import com.backend.suits.tasks.models.Task;
import com.backend.suits.tasks.models.TaskTime;
import com.backend.suits.tasks.repositories.TaskRepository;
import com.backend.suits.userProfile.models.UserProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTaskByLegalFileId(String legalFileId) {
        return taskRepository.findByLegalFileId(legalFileId);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getAllTasksByAssignee(UserProfile assignee) {
        return taskRepository.findByAssignee(assignee);
    }

    public Task createTask(Task task) {
        Task newTask = new Task(task.getLegalFileId(), task.getAssignee());
        return taskRepository.save(newTask);
    }




    public Task updateTask(Long taskId, @Valid Task task) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {
            Task taskToUpdate = optionalTask.get();

            taskToUpdate.setLegalFileId(task.getLegalFileId());
            taskToUpdate.setTitle(task.getTitle());
            taskToUpdate.setDescription(task.getDescription());
            taskToUpdate.setAssignee(task.getAssignee());
            taskToUpdate.setStatus(task.getStatus());
            taskToUpdate.setPriority(task.getPriority());
            taskToUpdate.setStartDate(task.getStartDate());
            taskToUpdate.setDueDate(task.getDueDate());
            taskToUpdate.setTaskTimes(task.getTaskTimes());
            taskToUpdate.setDescription(task.getDescription());
            taskToUpdate.setTaskNotes(task.getTaskNotes());

            return taskRepository.save(taskToUpdate);
        }else {
            throw new RuntimeException("La tâche à mettre à jour n'est pas trouvée.");
        }
    }

    public void deleteTask(Long taskId) {

        Task taskToDelete = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("La tâche à supprimer n'existe pas."));

        taskRepository.delete(taskToDelete);
    }

    public Task patchTask(Long taskId, Map<String, Object> updatedFields, UserProfile user) {

        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {
            Task taskToUpdate = optionalTask.get();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            updatedFields.forEach((key, value) -> {
                switch (key) {
                    case "title" -> taskToUpdate.setTitle((String) value);
                    case "description" -> taskToUpdate.setDescription((String) value);
                    case "priority" -> taskToUpdate.setPriority((Integer) value);
                    case "startDate" -> {
                            Instant instant = Instant.parse((String) value);
                            taskToUpdate.setStartDate(instant.atOffset(ZoneOffset.UTC).toLocalDate());
                    }
                    case "dueDate" -> {
                            Instant instant = Instant.parse((String) value);
                            taskToUpdate.setDueDate(instant.atOffset(ZoneOffset.UTC).toLocalDate());
                    }
                    case "status" -> taskToUpdate.setStatus( Status.valueOf((String) value));
                    case "legalFileId" -> taskToUpdate.setLegalFileId((String) value);
                    case "assignee" -> {
                        UserProfile assignee = objectMapper.convertValue(value, UserProfile.class);
                        taskToUpdate.setAssignee(assignee);
                    }
                    case "taskTimes" -> {
                        TaskTime taskTime = objectMapper.convertValue(value, TaskTime.class);
                        if(taskTime.getId() == -1) {
                            taskTime.setId(taskToUpdate.getTaskTimes().size() + 1);
                            taskToUpdate.getTaskTimes().add(taskTime);
                        }
                    }

                }
            });
            return taskRepository.save(taskToUpdate);

        }else {
            throw new RuntimeException("La tâche à mettre à jour n'est pas trouvée.");
        }
    }
}
