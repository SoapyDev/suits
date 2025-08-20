package com.backend.suits.tasks.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Component
@Documented
@Constraint(validatedBy = AssigneeValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAssignee {
    String message() default "Le responsable doit être un membre de l'équipe du dossier.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
