package com.backend.suits.conventionHonoraires.services;

import com.backend.suits.conventionHonoraires.entities.ConventionHonoraires;
import com.backend.suits.conventionHonoraires.repositories.ConventionHonorairesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConventionHonorairesService {

    ConventionHonorairesRepository conventionHonorairesRepository;

    @Autowired
    public ConventionHonorairesService(ConventionHonorairesRepository conventionHonorairesRepository) {
        this.conventionHonorairesRepository = conventionHonorairesRepository;
    }

    public ConventionHonoraires save(ConventionHonoraires conventionHonoraires) {
        return conventionHonorairesRepository.save(conventionHonoraires);
    }

    public ConventionHonoraires findByLegalFileId(String legalFileId) {

        return conventionHonorairesRepository.findByLegalFile_Id(legalFileId).orElse(null);
    }



    public void delete(Long conventionHonorairesId) {
        conventionHonorairesRepository.deleteById(conventionHonorairesId);
    }

    public Optional<ConventionHonoraires> getConventionById(long conventionHonorairesId) {
        return conventionHonorairesRepository.findById(conventionHonorairesId);
    }
}
