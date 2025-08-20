package com.backend.suits.legalFile.services;

import com.backend.suits.entity.User;
import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.legalFile.repositories.LegalFileRepository;
import com.backend.suits.organisation.models.TeamMembers;
import com.backend.suits.userProfile.models.UserProfile;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LegalFileService {

    private final LegalFileRepository legalFileRepository;

    @Autowired
    public LegalFileService(LegalFileRepository legalFileRepository) {
        this.legalFileRepository = legalFileRepository;
    }

    public LegalFile create(LegalFile legalFile) {

        String legalFieldPrefix = legalFile.getLawField().toString().substring(0, 3);

        String firstNamePrefix = legalFile.getFirstName().substring(0, 4).toUpperCase();

        String lastNamePrefix = legalFile.getLastName().substring(0,4).toUpperCase();

        String prefix = legalFieldPrefix + "-" + firstNamePrefix + lastNamePrefix + "-";

        Optional<String> lastId = legalFileRepository.findLatestIdByPrefix(prefix + '%');

        int nextNumber = lastId.map(id -> Integer.parseInt(id.substring(id.lastIndexOf('-') + 1)) + 1).orElse(1);

        String newId = prefix + String.format("%06d", nextNumber);

        legalFile.setId(newId);

        return legalFileRepository.save(legalFile);
    }

    public LegalFile findById(String id) {
        return legalFileRepository.findById(id).orElse(null);
    }

    public List<LegalFile> findAllProfessionalFiles(List<UserProfile> users) {
        return legalFileRepository.findLegalFilesByLegalFileTeamMembersContains(users);
    }

    public List<LegalFile> findAllClientFiles(@NonNull String email) {
        return legalFileRepository.findByEmail(email);
    }

    public void deleteById(String id) {
        LegalFile legalFileToDelete = legalFileRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Le dossier est inexistant" ));

        legalFileRepository.delete(legalFileToDelete);
    }

    public LegalFile updateDossier(LegalFile legalFile) {
        return legalFileRepository.save(legalFile);
    }
}
