package com.backend.suits.organisation.controllers;

import com.backend.suits.organisation.models.Organisation;
import com.backend.suits.organisation.services.OrganisationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/organisation")
public class OrganisationController {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final OrganisationService organisationService;

    public OrganisationController(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    @GetMapping
    public Organisation getOrganisation() {
        return organisationService.getOrganisation();
    }

    @PutMapping
    public Organisation updateOrganisation(@RequestBody Organisation organisation) {
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }

        return organisationService.updateOrganisation(organisation);
    }

}
