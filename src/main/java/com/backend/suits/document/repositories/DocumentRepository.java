package com.backend.suits.document.repositories;

import com.backend.suits.document.models.Document;
import com.backend.suits.tasks.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findDocumentsByLegalFileId(String legalFileId);
}
