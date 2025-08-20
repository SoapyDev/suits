package com.backend.suits.tasks.repositories;

import com.backend.suits.tasks.models.Task;
import com.backend.suits.userProfile.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByLegalFileId(String legalFileId);

    List<Task> findByAssignee(UserProfile assignee);
}

