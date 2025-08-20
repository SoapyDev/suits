package com.backend.suits.taskNotes.repositories;

import com.backend.suits.taskNotes.models.TaskNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskNoteRepository extends JpaRepository<TaskNote, Long> {
    TaskNote findTaskNoteById(Long taskNoteId);

}
