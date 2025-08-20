package com.backend.suits.conventionHonoraires.repositories;

import com.backend.suits.conventionHonoraires.entities.ConventionHonoraires;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConventionHonorairesRepository extends JpaRepository<ConventionHonoraires, Long> {

    Optional<ConventionHonoraires> findByLegalFile_Id(String legalFileId);
}
