package com.backend.suits.legalFile.repositories;

import com.backend.suits.entity.User;
import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.userProfile.models.UserProfile;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LegalFileRepository extends JpaRepository<LegalFile, String> {

    List<LegalFile> findByEmail(@NonNull String email);

    List<LegalFile> findLegalFilesByLegalFileTeamMembersContains(List<UserProfile> legalFileTeamMembers);

    @Query("SELECT d.id FROM LegalFile d WHERE d.id LIKE :prefix ORDER BY d.id DESC LIMIT 1")
    Optional<String> findLatestIdByPrefix(@Param("prefix") String prefix);

}
