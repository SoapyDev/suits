package com.backend.suits.conventionHonoraires.controllers;

import com.backend.suits.conventionHonoraires.entities.ConventionHonoraires;
import com.backend.suits.conventionHonoraires.models.ConventionHonorairesRequest;
import com.backend.suits.conventionHonoraires.models.ConventionId;
import com.backend.suits.conventionHonoraires.services.ConventionHonorairesService;
import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.legalFile.services.LegalFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
public class ConventionHonorairesController {

    private final String CONVENTION_HONORAIRES_GET = "/api/convention/get/{legalFileId}";
    private final String CONVENTION_HONORAIRES_CREATE = "/api/convention/create";
    private final String CONVENTION_HONORAIRES_ACCEPT = "/api/convention/accept";
    private final String CONVENTION_HONORAIRES_MODIFY = "/api/convention/modify";
    private final ConventionHonorairesService conventionHonorairesService;
    private final LegalFileService legalFileService;

    public ConventionHonorairesController(ConventionHonorairesService conventionHonorairesService, LegalFileService legalFileService) {
        this.conventionHonorairesService = conventionHonorairesService;
        this.legalFileService = legalFileService;
    }

    @GetMapping(CONVENTION_HONORAIRES_GET)
    public ResponseEntity<?> getConventionHonoraires(@PathVariable String legalFileId, Authentication authentication) {

        ConventionHonoraires conventionHonoraires = conventionHonorairesService.findByLegalFileId(legalFileId);

        if(this.legalFileService.findById(legalFileId) == null) {
            return ResponseEntity.notFound().build();
        }
        if(conventionHonoraires==null) {
            return ResponseEntity.noContent().build();
        }

        if (isClientInvolvedInLegalFile(conventionHonoraires.getLegalFile(), authentication)
                || isProfessionalInvolvedInLegalFile(conventionHonoraires.getLegalFile(), authentication)) {

            return ResponseEntity.ok(conventionHonoraires);

        } else {
            return ResponseEntity.status(403).body("Vous n'avez pas accès à cette convention d'honoraires.");
        }
    }

    @PutMapping(CONVENTION_HONORAIRES_CREATE)
    public ResponseEntity<?> createConventionHonoraires(
            @RequestBody ConventionHonorairesRequest conventionHonorairesRequest, Authentication authentication) {

        if (isFileAgreementOnFile(conventionHonorairesRequest.legalFileId())){

            return ResponseEntity.status(400).body("Une convention d'honoraires est déjà au dossier.");
        }
        System.out.println("Num de dossier" + conventionHonorairesRequest);

        LegalFile legalFile = legalFileService.findById(conventionHonorairesRequest.legalFileId());

        if (isProfessionalInvolvedInLegalFile(legalFile, authentication)) {
            System.out.println("Professional involved");
            ConventionHonoraires conventionHonoraires = new ConventionHonoraires();

            conventionHonoraires.setCreator(authentication.getName());
            conventionHonoraires.setLegalFile(legalFile);
            conventionHonoraires.setEstimationPrix(conventionHonorairesRequest.estimationPrix());
            conventionHonoraires.setMandat(conventionHonorairesRequest.mandat());
            conventionHonoraires.setTarifHoraire(conventionHonorairesRequest.tarifHoraire());
            conventionHonoraires.setDateEntente(LocalDate.now());
            conventionHonoraires.setAgreement(conventionHonorairesRequest.agreement());

            conventionHonorairesService.save(conventionHonoraires);

            System.out.println(conventionHonoraires);

            return ResponseEntity.ok(conventionHonoraires);
        } else {

            return ResponseEntity.status(403).body("Vous n'êtes pas autorisé à ajouter la convention d'honoraires.");

        }

    }

    @PutMapping(CONVENTION_HONORAIRES_MODIFY)
    public ResponseEntity<?> modifyConventionHonoraires(
            @RequestBody ConventionHonorairesRequest conventionHonorairesRequest, Authentication authentication){

        ConventionHonoraires conventionHonoraires =
                conventionHonorairesService.findByLegalFileId(conventionHonorairesRequest.legalFileId());

        if (!isProfessionalModifyingAgreementTheCreator(conventionHonoraires, authentication)){
            return ResponseEntity.status(403).body("Vous ne pouvez pas modifier la convention d'honoraires.");
        } else {
            System.out.println("Test sur modif");
            conventionHonoraires.setMandat(conventionHonorairesRequest.mandat());
            conventionHonoraires.setEstimationPrix(conventionHonorairesRequest.estimationPrix());
            conventionHonoraires.setDateEntente(LocalDate.now());
            conventionHonoraires.setIsApproved(false);
            conventionHonoraires.setApprovalDate(null);
            conventionHonoraires.setTarifHoraire(conventionHonorairesRequest.tarifHoraire());

            conventionHonorairesService.save(conventionHonoraires);

            return ResponseEntity.ok(conventionHonoraires);
        }

    }


    @PostMapping(CONVENTION_HONORAIRES_ACCEPT)
    public ResponseEntity<?> acceptConventionHonoraires(@RequestBody ConventionId id, Authentication authentication) {

        Optional<ConventionHonoraires> conventionHonoraires =
                conventionHonorairesService.getConventionById(id.id());

        if(conventionHonoraires.isEmpty()){
            return ResponseEntity.status(400).body("Aucune convention d'honoraires au dossier.");
        } else if(isClientInvolvedInLegalFile(conventionHonoraires.get().getLegalFile(), authentication)){

            conventionHonoraires.get().setApprovalDate(LocalDate.now());
            conventionHonoraires.get().setIsApproved(true);

            conventionHonorairesService.save(conventionHonoraires.get());

            return ResponseEntity.ok(conventionHonoraires.get());
        } else {

            return ResponseEntity.status(403).body("Vous n'êtes pas autorisé à approuver cette convention d'honoraires.");

        }
    }


    private boolean isClientInvolvedInLegalFile(LegalFile legalFile, Authentication authentication) {

        return legalFile.getEmail().equals(authentication.getName());
    }

    private boolean isProfessionalInvolvedInLegalFile(LegalFile legalFile, Authentication authentication) {

        boolean isInvolved = false;

        for(int i = 0; i < legalFile.getLegalFileTeamMembers().size(); i++){
            if(legalFile.getLegalFileTeamMembers().get(i).getUsername().equals(authentication.getName())){
                isInvolved = true;
            }
        }

        return isInvolved;
    }

    private boolean isProfessionalModifyingAgreementTheCreator(ConventionHonoraires conventionHonoraires, Authentication authentication) {

        return conventionHonoraires.getCreator().equals(authentication.getName());
    }

    private boolean isFileAgreementOnFile(String legalFileId) {

        return conventionHonorairesService.findByLegalFileId(legalFileId) != null;

    }


}
