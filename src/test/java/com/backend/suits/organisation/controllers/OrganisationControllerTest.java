package com.backend.suits.organisation.controllers;

import com.backend.suits.organisation.models.Organisation;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase?autoReconnect=true")
public class OrganisationControllerTest {

    private static final String TEST = "test";


    @Autowired
    private OrganisationController controller;

    private Organisation organisation;

    @BeforeEach
    public void setUp() {
        organisation = controller.getOrganisation();
    }

    @Test
    public void getOrganisation() {
        assert(organisation != null);
        assertEquals("Avocats Associés Durand & Legrand", organisation.getName());
        assertEquals("Notre cabinet d’avocats est fondé sur des valeurs de\nprofessionnalisme, de rigueur et de dévouement au service de la justice. Situé au cœur de\nMontréal, nous accompagnons nos clients dans toutes leurs démarches juridiques avec\nexpertise et intégrité.", organisation.getShortDescription());
        assertEquals("Notre équipe pluridisciplinaire est composée d’avocats expérimentés spécialisés en droit des\naffaires, droit du travail, droit immobilier, famille et patrimoine. Nous mettons à votre\nservice notre connaissance approfondie du droit pour vous fournir un conseil personnalisé\nadapté à vos besoins spécifiques.\n\nChez Avocats Associés Durand & Legrand, nous privilégions la proximité avec nos clients.\nNotre objectif est de construire une relation de confiance durable basée sur l’écoute et le\ndialogue. Nous vous accompagnons de manière proactive tout au long des procédures judiciaires\nou amiables pour obtenir les meilleurs résultats possibles.\n\nNous sommes résolument tournés vers l'avenir, en intégrant constamment les nouvelles\ntechnologies et les évolutions législatives afin d’offrir une assistance juridique innovante\net performante. Nous restons à votre disposition pour toute consultation ou question\nconcernant vos droits et obligations.\n\nContactez-nous pour découvrir comment Avocats Associés Durand & Legrand peut vous aider à\nnaviguer dans le monde complexe du droit, avec efficacité et sérénité.", organisation.getLongDescription());
        assertEquals("2163243423", organisation.getNeq());
        assertEquals("12345678", organisation.getTaxGSTId());
        assertEquals("987654321", organisation.getTaxQSTId());
        assertEquals("322 Rue Bleignier", organisation.getAddress());
        assertEquals("Montréal", organisation.getCity());
        assertEquals("Canada", organisation.getCountry());
        assertEquals("H4A 2A2", organisation.getPostalCode());
        assertEquals("Québec", organisation.getProvince());
        assertEquals("123 123-1234", organisation.getPhone());
        assertEquals("info@avocatsdurandlegrand.outlook.com", organisation.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void updateOrganisation() {
        try {
            controller.updateOrganisation(organisation);
            assert (true);
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    public void updateInvalidOrganisation() {

        Organisation organisation = getMutatedOrganisation();

        try {
            controller.updateOrganisation(organisation);
            assert (false);
        } catch (Exception e) {
            assert (true);
        }

    }

    private Organisation getMutatedOrganisation() {
        organisation.setName(TEST);
        organisation.setShortDescription(TEST);
        organisation.setLongDescription(TEST);
        organisation.setNeq(TEST);
        organisation.setTaxGSTId(TEST);
        organisation.setTaxQSTId(TEST);
        organisation.setAddress(TEST);
        organisation.setCity(TEST);
        organisation.setCountry(TEST);
        organisation.setPostalCode(TEST);
        organisation.setProvince(TEST);
        organisation.setPhone(TEST);
        organisation.setEmail(TEST);
        return organisation;
    }
}
