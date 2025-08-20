package com.backend.suits.document.controllers;

import com.backend.suits.document.models.Document;
import com.backend.suits.document.services.SupabaseStorageService;
import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.legalFile.services.LegalFileService;
import com.backend.suits.userProfile.models.UserProfile;
import com.backend.suits.userProfile.services.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DocumentController {

    private final SupabaseStorageService storageService;
    private final UserProfileService userProfileService;
    private final LegalFileService legalFileService;

    public DocumentController(SupabaseStorageService storageService, UserProfileService userProfileService, LegalFileService legalFileService) {
        this.storageService = storageService;
        this.userProfileService = userProfileService;
        this.legalFileService = legalFileService;
    }

    @GetMapping("/api/dossier/{dossierId}/documents")
    public ResponseEntity<?> getDocuments(@PathVariable String dossierId,
                                            Authentication authentication) {
        UserProfile userUploading = userProfileService.findByUsername(authentication.getName());

        LegalFile legalFile = legalFileService.findById(dossierId);

        if(!legalFile.getLegalFileTeamMembers().contains(userUploading)) {
            return ResponseEntity.status(403).body("Vous n'avez pas les autorisations pour lire les documents sur ce dossier.");
        }

        try {

            List<Document> documents = storageService.getFiles(dossierId);

            for (Document doc : documents) {

                String signedUrl = storageService.generateSignedUrl(doc.getStoragePath(), 600); // 10 min
                doc.setSignedUrl(signedUrl);
            }
            return ResponseEntity.ok(documents);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la lecture : " + e.getMessage());
        }
    }

    @PostMapping("/api/dossier/{dossierId}/documents")
    public ResponseEntity<?> uploadDocument(@PathVariable String dossierId,
                                            @RequestParam("file") MultipartFile file,
                                            @RequestParam("fileDescription") String fileDescription,
                                            Authentication authentication) {

        UserProfile userUploading = userProfileService.findByUsername(authentication.getName());

        LegalFile legalFile = legalFileService.findById(dossierId);

        if(!legalFile.getLegalFileTeamMembers().contains(userUploading)) {
            return ResponseEntity.status(403).body("Vous n'avez pas les autorisations pour charger un document sur ce dossier.");
        }

        try {
            storageService.uploadFile(dossierId, file,fileDescription, userUploading);
            return ResponseEntity.ok(Collections.singletonMap("message", "Document téléversé !"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'envoi : " + e.getMessage());
        }
    }

    @DeleteMapping("/api/dossier/{dossierId}/documents/{documentId}")
    public ResponseEntity<?> DeleteDocument(@PathVariable("dossierId") String dossierId,
                                            @PathVariable("documentId") Long documentId,
                                            Authentication authentication) {

        UserProfile userUploading = userProfileService.findByUsername(authentication.getName());

        LegalFile legalFile = legalFileService.findById(dossierId);

        if(!legalFile.getLegalFileTeamMembers().contains(userUploading)) {
            return ResponseEntity.status(403).body("Vous n'avez pas les autorisations pour supprimer ce document sur ce dossier.");
        }

        try {
            storageService.deleteFile(dossierId, documentId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Document supprimé !");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'envoi : " + e.getMessage());
        }
    }
}
