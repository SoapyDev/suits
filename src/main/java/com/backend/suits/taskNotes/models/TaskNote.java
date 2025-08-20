package com.backend.suits.taskNotes.models;

import com.backend.suits.tasks.models.Task;
import com.backend.suits.userProfile.models.UserProfile;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate creationDate;

    private LocalDate updateDate;

    private String content;

    @ManyToOne
    @JoinColumn(name = "creator_username", referencedColumnName = "username", nullable = false)
    @JsonIgnoreProperties({"taskNotes"})
    private UserProfile creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id",referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private Task task;

    public TaskNote(UserProfile creator, LocalDate creationDate, String content, Task task) {
        this.creator = creator;
        this.creationDate = creationDate;
        this.content = content;
        this.task = task;
    }
}
