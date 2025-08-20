package com.backend.suits.tasks.models;

import com.backend.suits.taskNotes.models.TaskNote;
import com.backend.suits.userProfile.models.UserProfile;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    public Task(String legalFileId, UserProfile assignee) {
        this.legalFileId = legalFileId;
        this.title = " ";
        this.assignee = assignee;
        this.status = Status.NON_DEBUTE;
        this.priority = 3;
        this.startDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(1);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le dossier associée à la tâche est obligatoire.")
    private String legalFileId;

    @Column(length = 100)
    @Size(max=100, message = "Le titre d'une tâche ne peut pas avoir plus de 100 caractères")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "assignee_username")
    private UserProfile assignee;


    @Enumerated(EnumType.STRING)
    private Status status;

    @Min(value=1,message = "La priorité doit être comprise entre 1 et 5.")
    @Max(value=5,message = "La priorité doit être comprise entre 1 et 5.")
    private int priority = 3; // Valeur 3 par défault

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate= LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @FutureOrPresent(message="La date de fin ne peut pas être une date passée")
    private LocalDate dueDate = LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    @ElementCollection
    @CollectionTable(name = "task_times", joinColumns = @JoinColumn(name = "task_id"))
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "name")),
            @AttributeOverride(name = "time", column = @Column(name = "time")),
            @AttributeOverride(name = "tarification", column = @Column(name = "tarification"))
    })
    private List<TaskTime> taskTimes;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TaskNote> taskNotes= new ArrayList<>();

    @AssertTrue(message = "La date de fin de la tâche doit être après la date de début.")
    public boolean isDueDateValid() {
        return dueDate != null && startDate != null && dueDate.isAfter(startDate);
    }


}
