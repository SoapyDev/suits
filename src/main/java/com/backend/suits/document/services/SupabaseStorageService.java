package com.backend.suits.document.services;

import com.backend.suits.document.models.Document;
import com.backend.suits.document.models.SignedUrlResponse;
import com.backend.suits.document.repositories.DocumentRepository;
import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.legalFile.repositories.LegalFileRepository;
import com.backend.suits.legalFile.services.LegalFileService;
import com.backend.suits.userProfile.models.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SupabaseStorageService {

    private final WebClient webClient;
    private final LegalFileService legalFileService;
    private final LegalFileRepository legalFileRepository;
    private final DocumentRepository documentRepository;

    @Value("${supabase.storage.url}")
    private String baseUrl;

    @Value("${supabase.api.key}")
    private String apiKey;

    @Value("${supabase.bucket}")
    private String bucket;

    @Autowired
    public SupabaseStorageService(WebClient webClient, LegalFileService legalFileService, LegalFileRepository legalFileRepository, DocumentRepository documentRepository) {
        this.webClient = webClient;
        this.legalFileService = legalFileService;
        this.legalFileRepository = legalFileRepository;
        this.documentRepository = documentRepository;
    }

    public void uploadFile(String legalFileId, MultipartFile file, String fileDescription, UserProfile user) throws IOException {

        String fileName = file.getOriginalFilename();

        LegalFile legalFile = legalFileService.findById(legalFileId);
        if (legalFile == null) {
            throw new RuntimeException("Erreur lors de la sauvegarde: Le dossier légal n'a pas été trouvé");
        }
        String path = "legalFiles/" + legalFileId + "/" + fileName;
        String storagePath = baseUrl + "/object/" + bucket + "/" + path;

        webClient.post()
                .uri(baseUrl + "/object/" + bucket + "/" + path)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header("apikey", apiKey)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(file.getBytes())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->
                                Mono.error(new RuntimeException("Erreur Supabase : " + errorBody))
                        )
                )
                .bodyToMono(Void.class)
                .block();

        Document document = new Document();
        document.setLegalFile(legalFile);
        document.setName(fileName);
        document.setStoragePath(storagePath);
        document.setFileSize(file.getSize());
        document.setContentType(file.getContentType());
        document.setDescription(fileDescription);
        document.setUploadedDate(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        document.setUploadedBy(user);

        legalFile.getDocuments().add(document);
        documentRepository.save(document);
        legalFileRepository.save(legalFile);
    }

    public void deleteFile(String legalFileId, Long documentId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Le document avec l'ID " + documentId + " n'a pas été trouvé"));

        LegalFile legalFile = legalFileRepository.findById(legalFileId)
                .orElseThrow(() -> new RuntimeException("Le dossier légal avec l'ID " + legalFileId + " n'a pas été trouvé"));

        webClient.delete()
                .uri(document.getStoragePath())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header("apikey", apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->
                                Mono.error(new RuntimeException("Erreur Supabase : " + errorBody))
                        )
                )
                .bodyToMono(Void.class)
                .block();

        documentRepository.delete(document);

        legalFile.getDocuments().remove(document);
        legalFileRepository.save(legalFile);
    }

    public List<Document> getFiles(String dossierId) {
        return documentRepository.findDocumentsByLegalFileId(dossierId);
    }

    public String generateSignedUrl(String storagePath, int expiresInSeconds) {
        String requestBody = String.format("{\"expiresIn\": %d}", expiresInSeconds);

        String path = storagePath.replace(baseUrl + "/object/" + bucket + "/", "");

        SignedUrlResponse response = webClient.post()
                .uri(baseUrl + "/object/sign/" + bucket + "/" + path)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header("apikey", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res ->
                        res.bodyToMono(String.class).flatMap(errorBody ->
                                Mono.error(new RuntimeException("Erreur Supabase : " + errorBody))
                        )
                )
                .bodyToMono(SignedUrlResponse.class)
                .block();

        if (response == null || response.getSignedUrl() == null) {
            throw new RuntimeException("Échec lors de la génération du lien signé Supabase.");
        }

        return baseUrl + response.getSignedUrl();
    }

}

