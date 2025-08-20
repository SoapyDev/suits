package com.backend.suits.legalFile.controllers;

import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.legalFile.services.LegalFileService;
import com.backend.suits.service.UserService;
import com.backend.suits.tasks.models.Status;
import com.backend.suits.userProfile.models.UserProfile;
import com.backend.suits.userProfile.services.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class LegalFileController {

    private static final String CREATE_DOSSIER = "/api/dossier";
    private static final String GET_DOSSIERS_CLIENT = "/api/dossier/client";
    private static final String GET_DOSSIER_CLIENT = "/api/dossier/client/{dossierId}";
    private static final String GET_DOSSIERS_PROFESSIONNEL = "/api/dossier/professionnel";
    private static final String GET_DOSSIER_PROFESSIONNEL = "/api/dossier/professionnel/{dossierId}";
    private static final String UPDATE_DOSSIER_PROFESSIONNEL = "/api/dossier/professionnel/{dossierId}";
    private static final String GET_TEAM_MEMBERS = "/api/dossier/{dossierId}/teammembers";

    private final UserService userService;
    private final LegalFileService legalFileService;
    private final UserProfileService userProfileService;

    public LegalFileController(UserService userService, LegalFileService legalFileService, UserProfileService userProfileService) {
        this.userService = userService;
        this.legalFileService = legalFileService;
        this.userProfileService = userProfileService;
    }

    @PostMapping(CREATE_DOSSIER)
    public ResponseEntity<?> createNewClientFile(@RequestBody LegalFile legalFile, Authentication authentication) {

        UserProfile fileCreator = userProfileService.findByUsername(authentication.getName());

        List<UserProfile> legalFileTeamMembers = List.of(fileCreator);

        legalFile.setLegalFileTeamMembers(legalFileTeamMembers);
        LegalFile createdFile = legalFileService.create(legalFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdFile);
    }

    private boolean getLegalFileCreator(String fileCreatorName){

        return userService.findByUsername(fileCreatorName).isPresent();

    }

    @GetMapping(GET_DOSSIERS_CLIENT)
    public ResponseEntity<?> getClientLegalFiles(Authentication authentication) {

        List<LegalFile> legalFiles = legalFileService.findAllClientFiles(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(legalFiles);
    }

    @GetMapping(GET_DOSSIERS_PROFESSIONNEL)
    public ResponseEntity<?> getProfessionnelLegalFiles(Authentication authentication) {

        UserProfile teamMember = userProfileService.findByUsername(authentication.getName());
        List<UserProfile> teamMembers = List.of(teamMember);
        List<LegalFile> legalFiles = legalFileService.findAllProfessionalFiles(teamMembers);
        return ResponseEntity.status(HttpStatus.OK).body(legalFiles);
    }

    @GetMapping(GET_DOSSIER_CLIENT)
    public ResponseEntity<?> getClientLegalFile(@PathVariable String dossierId, Authentication authentication) {
        LegalFile legalFile = legalFileService.findById(dossierId);

        if(isClientInvolvedInLegalFile(legalFile, authentication)){
            return ResponseEntity.status(HttpStatus.OK).body(legalFile);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'avez pas accès à ce dossier");
    }

    @GetMapping(GET_DOSSIER_PROFESSIONNEL)
    public ResponseEntity<?> getProfessionnelLegalFile(@PathVariable String dossierId, Authentication authentication) {
        LegalFile legalFile = legalFileService.findById(dossierId);

        if(isProfessionalInvolvedInLegalFile(legalFile, authentication)){

            return ResponseEntity.status(HttpStatus.OK).body(legalFile);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'avez pas accès à ce dossier");
    }

    @GetMapping(GET_TEAM_MEMBERS)
    public ResponseEntity<?> getTeamMembers(@PathVariable String dossierId, Authentication authentication) {

        LegalFile legalFile = legalFileService.findById(dossierId);

        if(isProfessionalInvolvedInLegalFile(legalFile, authentication)){

            List<UserProfile> teamMembers = legalFile.getLegalFileTeamMembers();
            return ResponseEntity.status(HttpStatus.OK).body(teamMembers);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'avez pas accès à ce dossier");
    }

    @PutMapping(GET_TEAM_MEMBERS)
    public ResponseEntity<?> setTeamMembers(@PathVariable String dossierId,
                                            @RequestBody  List<UserProfile> teamMembers,
                                            Authentication authentication) {

        System.out.println("TeamMembers" +teamMembers);
        LegalFile legalFile = legalFileService.findById(dossierId);

            legalFile.setLegalFileTeamMembers(teamMembers);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.legalFileService.updateDossier(legalFile));
    }

    @PutMapping(UPDATE_DOSSIER_PROFESSIONNEL)
    public ResponseEntity<LegalFile> updateProfessionnelLegalFile(@RequestBody LegalFile legalFile) {

        if(legalFile.getStatus().equals(Status.TERMINEE)){

            legalFile.setEndOfMandate(LocalDate.now());
            legalFile.setClosed(true);
        } else {
            legalFile.setEndOfMandate(null);
            legalFile.setClosed(false);
        }

        return ResponseEntity.status(HttpStatus.OK).body(legalFileService.updateDossier(legalFile));
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
}
