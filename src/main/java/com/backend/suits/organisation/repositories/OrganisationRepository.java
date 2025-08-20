package com.backend.suits.organisation.repositories;

import com.backend.suits.organisation.models.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {}
