package com.backend.suits.tasks.validation;

import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.legalFile.repositories.LegalFileRepository;
import com.backend.suits.tasks.models.Task;
import com.backend.suits.userProfile.models.UserProfile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class AssigneeValidator implements ConstraintValidator<ValidAssignee, Task> {

    private final LegalFileRepository legalFileRepository;

    @Autowired
    public AssigneeValidator(LegalFileRepository legalFileRepository) {
        this.legalFileRepository = legalFileRepository;
    }

    @Override
    public boolean isValid(Task task, ConstraintValidatorContext context) {
        if (task.getAssignee() == null || task.getLegalFileId() == null) {
            return false;
        }

        LegalFile legalFile = legalFileRepository.findById(task.getLegalFileId()).orElse(null);

        if (legalFile == null) {
            return false;
        }

        return legalFile.getLegalFileTeamMembers().contains(task.getAssignee());

    }
}
