package com.backend.suits.organisation.services;

import com.backend.suits.organisation.models.Organisation;
import com.backend.suits.organisation.repositories.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class OrganisationService {

    private final ResourceLoader resourceLoader;
    private final OrganisationRepository repository;

    @Autowired
    public OrganisationService(OrganisationRepository repository, ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.repository = repository;
    }

    public Organisation getOrganisation() {
        List<Organisation> organisations = repository.findAll();

        if (organisations.isEmpty()) {
            return createOrganisation();
        }

        return organisations.get(0);
    }

    public Organisation updateOrganisation(Organisation organisation) {
        return repository.save(organisation);
    }

    private Organisation createOrganisation() {
        Resource resource = resourceLoader.getResource("classpath:static/organisation.json");

        if (!resource.exists()) throw new RuntimeException("Le fichier organisation.json n'a pas été trouvé");

        try {

            Organisation organisation = Organisation.from_json(new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
            return repository.save(organisation);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'instanciation de l'organisation : " + e.getMessage());
        }
    }
}
