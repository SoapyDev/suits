package com.backend.suits.tasks.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Validator validator;
    private Task task;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        task = Task.builder()
                .title("Task Title")
                .legalFileId("LegalFile Id")
                .startDate(LocalDate.parse("2025-03-10"))
                .dueDate(LocalDate.parse("2025-04-15"))
                .build();
    }

    @Test
    void testEmptyTask() {
        Task emptyTask = new Task();

        Set<ConstraintViolation<Task>> violations = validator.validate(emptyTask);
        assertEquals(1, violations.size());
    }

    @Test
    void testTaskWithRequiredFields() {
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertEquals(0, violations.size());
    }

    @Test
    void testTaskWithBadTitle() {
        task.setTitle("Rédaction et validation du rapport annuel détaillé sur la performance organisationnelle et les objectifs futurs");
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertEquals(1, violations.size());
        assertEquals("Le titre d'une tâche ne peut pas avoir plus de 100 caractères", violations.iterator().next().getMessage());
    }

    @Test
    void testTaskWithGoodStatus() {
        task.setStatus(Status.EN_COURS);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertEquals(0, violations.size());

    }

    @Test
    void testTaskWithBadPriorityMax() {
        task.setPriority(8);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertEquals(1, violations.size());
        assertEquals("La priorité doit être comprise entre 1 et 5.", violations.iterator().next().getMessage());
    }

    @Test
    void testTaskWithBadPriorityMin() {
        task.setPriority(0);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertEquals(1, violations.size());
        assertEquals("La priorité doit être comprise entre 1 et 5.", violations.iterator().next().getMessage());
    }

    @Test
    void testTaskWithGoodPriority() {
        task.setPriority(5);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertEquals(0, violations.size());
    }

    @Test
    void testTaskWithDueDateInThePast() {
        task.setDueDate(LocalDate.parse("2025-03-12"));
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertEquals(1, violations.size());
        assertEquals("La date de fin ne peut pas être une date passée", violations.iterator().next().getMessage());
    }

//    @Test
//    void testTaskWithDueDateBeforeStartDate() {
//        task.setStartDate(LocalDate.now().plusDays(30));
//        task.setDueDate(LocalDate.now().plusDays(10));
//        Set<ConstraintViolation<Task>> violations = validator.validate(task);
//
//        assertEquals(0, violations.size());
//        assertEquals("La date de fin de la tâche doit être après la date de début.", violations.iterator().next().getMessage());
//    }




}