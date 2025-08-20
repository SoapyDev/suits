package com.backend.suits.legalFile.controllers;

import com.backend.suits.entity.Role;
import com.backend.suits.entity.User;
import com.backend.suits.legalFile.entities.LawField;
import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.legalFile.entities.PaymentAgreement;
import com.backend.suits.legalFile.models.LegalFileRequest;
import com.backend.suits.legalFile.services.LegalFileService;
import com.backend.suits.service.UserService;
import com.backend.suits.userProfile.models.Profession;
import com.backend.suits.userProfile.models.UserProfile;
import com.backend.suits.userProfile.services.UserProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LegalFileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LegalFileService legalFileService;

    @Mock
    UserService userService;

    @Mock
    UserProfileService userProfileService;

    @InjectMocks
    private LegalFileController legalFileController;


    private ObjectMapper objectMapper;
    LegalFile legalFile;
    List<User> legalFileTeamMembers = new ArrayList<>();

    String usernameProfessional = "profesional@avocat.com";
    String usernameClient = "john.smith@client.com";

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        MockitoAnnotations.openMocks(this);

        legalFileController = new LegalFileController(userService,legalFileService, userProfileService);
        mockMvc = MockMvcBuilders.standaloneSetup(legalFileController).build();

    }

    @AfterEach
    void tearDown() {

        legalFileTeamMembers.clear();

    }

    @Test
    void createNewClientFile() throws Exception {

        UserProfile fileCreator = new UserProfile();
        fileCreator.setUsername(usernameProfessional);
        fileCreator.setProfession(Profession.AVOCAT);
        when(userProfileService.findByUsername(usernameProfessional)).thenReturn(fileCreator);

        LegalFileRequest legalFileRequest = new LegalFileRequest(
                "John",
                "Smith",
                usernameClient,
                "4505551234",
                "123",
                "St-Hubert",
                "Montreal",
                "QC",
                "H0H0H0",
                LocalDate.parse("1960-01-01"),
                LocalDate.parse("2025-03-28"),
                LawField.PENAL,
                PaymentAgreement.HORAIRE,
                usernameProfessional
                );

        LegalFile legalFile = new LegalFile();
        legalFile.setId("PEN-SMITJOHN-000001");
        legalFile.setFirstName(legalFileRequest.firstName());
        legalFile.setLastName(legalFileRequest.lastName());
        legalFile.setEmail(legalFileRequest.email());
        legalFile.setPhoneNumber(legalFileRequest.phoneNumber());
        legalFile.setCivicNumber(legalFileRequest.civicNumber());
        legalFile.setStreetName(legalFileRequest.streetName());
        legalFile.setCity(legalFileRequest.city());
        legalFile.setProvince(legalFileRequest.province());
        legalFile.setPostalCode(legalFileRequest.postalCode());
        legalFile.setBirthdate(legalFileRequest.birthDate());
        legalFile.setStartOfMandate(legalFileRequest.startOfMandate());
        legalFile.setLawField(legalFileRequest.lawField());
        legalFile.setAgreement(legalFileRequest.agreementType());
        legalFile.setLegalFileTeamMembers(List.of(fileCreator));

        when(legalFileService.create(any(LegalFile.class))).thenReturn(legalFile);

        // Perform POST request
        mockMvc.perform(post("/api/dossier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(legalFileRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(legalFile.getId()))
                .andExpect(jsonPath("$.email").value(legalFile.getEmail()))
                .andExpect(jsonPath("$.firstName").value(legalFile.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(legalFile.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(legalFile.getPhoneNumber()))
                .andExpect(jsonPath("$.city").value(legalFile.getCity()));


    }

    @Test
    void getClientLegalFiles() throws Exception {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(usernameClient);

        UserProfile fileCreator = new UserProfile();
        fileCreator.setUsername(usernameProfessional);
        fileCreator.setProfession(Profession.AVOCAT);


        LegalFile legalFile1 = new LegalFile();
        legalFile1.setId("LOG-BOUCBOBB-0001");
        legalFile1.setFirstName("Bobby");
        legalFile1.setLastName("Boucher");
        legalFile1.setEmail(usernameClient);
        legalFile1.setPhoneNumber( "4505551234");
        legalFile1.setCivicNumber("123");
        legalFile1.setStreetName("St-Hubert");
        legalFile1.setCity("Montreal");
        legalFile1.setProvince("QC");
        legalFile1.setPostalCode("H0H0H0");
        legalFile1.setBirthdate(LocalDate.parse("1970-01-01"));
        legalFile1.setStartOfMandate(LocalDate.parse("2025-03-28"));
        legalFile1.setLawField(LawField.LOGEMENT);
        legalFile1.setAgreement(PaymentAgreement.HORAIRE);
        legalFile1.setLegalFileTeamMembers(List.of(fileCreator));


        LegalFile legalFile2 = new LegalFile();
        legalFile1.setId("LOG-BOUCBOBB-0002");
        legalFile1.setFirstName("Bobby");
        legalFile1.setLastName("Boucher");
        legalFile1.setEmail(usernameClient);
        legalFile1.setPhoneNumber( "4505551234");
        legalFile1.setCivicNumber("123");
        legalFile1.setStreetName("St-Hubert");
        legalFile1.setCity("Montreal");
        legalFile1.setProvince("QC");
        legalFile1.setPostalCode("H0H0H0");
        legalFile1.setBirthdate(LocalDate.parse("1970-01-01"));
        legalFile1.setStartOfMandate(LocalDate.parse("2025-03-28"));
        legalFile1.setLawField(LawField.LOGEMENT);
        legalFile1.setAgreement(PaymentAgreement.HORAIRE);
        legalFile1.setLegalFileTeamMembers(List.of(fileCreator));

        List<LegalFile> clientLegalFiles = List.of(legalFile1, legalFile2);

        // Mock service response
        when(legalFileService.findAllClientFiles(usernameClient)).thenReturn(clientLegalFiles);

        // Perform GET request with authentication
        mockMvc.perform(get("/api/dossier/client")
                        .principal(authentication))  // Simulating the authenticated user
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(clientLegalFiles.size()))
                .andExpect(jsonPath("$[0].id").value(legalFile1.getId()))
                .andExpect(jsonPath("$[0].email").value(legalFile1.getEmail()))
                .andExpect(jsonPath("$[1].id").value(legalFile2.getId()))
                .andExpect(jsonPath("$[1].email").value(legalFile2.getEmail()));

    }

    @Test
    void getProfessionnelLegalFiles() throws Exception {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(usernameProfessional);

        UserProfile fileCreator1 = new UserProfile();
        fileCreator1.setUsername(usernameProfessional);
        fileCreator1.setProfession(Profession.AVOCAT);

        UserProfile fileCreator2 = new UserProfile();
        fileCreator2.setUsername("test@test.com");
        fileCreator2.setProfession(Profession.AVOCAT);

        when(userProfileService.findByUsername(usernameProfessional)).thenReturn(fileCreator1);

        LegalFile legalFile1 = new LegalFile();
        legalFile1.setId("LOG-BOUCBOBB-0001");
        legalFile1.setFirstName("Bobby");
        legalFile1.setLastName("Boucher");
        legalFile1.setEmail(usernameClient);
        legalFile1.setPhoneNumber( "4505551234");
        legalFile1.setCivicNumber("123");
        legalFile1.setStreetName("St-Hubert");
        legalFile1.setCity("Montreal");
        legalFile1.setProvince("QC");
        legalFile1.setPostalCode("H0H0H0");
        legalFile1.setBirthdate(LocalDate.parse("1970-01-01"));
        legalFile1.setStartOfMandate(LocalDate.parse("2025-03-28"));
        legalFile1.setLawField(LawField.LOGEMENT);
        legalFile1.setAgreement(PaymentAgreement.HORAIRE);
        legalFile1.setLegalFileTeamMembers(List.of(fileCreator1));


        LegalFile legalFile2 = new LegalFile();
        legalFile2.setId("LOG-BOUCBOBB-0002");
        legalFile2.setFirstName("Bobby");
        legalFile2.setLastName("Boucher");
        legalFile2.setEmail(usernameClient);
        legalFile2.setPhoneNumber( "4505551234");
        legalFile2.setCivicNumber("123");
        legalFile2.setStreetName("St-Hubert");
        legalFile2.setCity("Montreal");
        legalFile2.setProvince("QC");
        legalFile2.setPostalCode("H0H0H0");
        legalFile2.setBirthdate(LocalDate.parse("1970-01-01"));
        legalFile2.setStartOfMandate(LocalDate.parse("2025-03-28"));
        legalFile2.setLawField(LawField.LOGEMENT);
        legalFile2.setAgreement(PaymentAgreement.HORAIRE);
        legalFile2.setLegalFileTeamMembers(List.of(fileCreator1));

        LegalFile legalFile3 = new LegalFile();
        legalFile3.setId("LOG-BOUCBOBB-0002");
        legalFile3.setFirstName("Bobby");
        legalFile3.setLastName("Boucher");
        legalFile3.setEmail(usernameClient);
        legalFile3.setPhoneNumber( "4505551234");
        legalFile3.setCivicNumber("123");
        legalFile3.setStreetName("St-Hubert");
        legalFile3.setCity("Montreal");
        legalFile3.setProvince("QC");
        legalFile3.setPostalCode("H0H0H0");
        legalFile3.setBirthdate(LocalDate.parse("1970-01-01"));
        legalFile3.setStartOfMandate(LocalDate.parse("2025-03-28"));
        legalFile3.setLawField(LawField.LOGEMENT);
        legalFile3.setAgreement(PaymentAgreement.HORAIRE);
        legalFile3.setLegalFileTeamMembers(List.of(fileCreator2));


        List<LegalFile> professionalFiles = List.of(legalFile1, legalFile2);

        when(legalFileService.findAllProfessionalFiles(List.of(fileCreator1))).thenReturn(professionalFiles);

        mockMvc.perform(get("/api/dossier/professionnel")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(professionalFiles.size()))
                .andExpect(jsonPath("$[0].id").value(legalFile1.getId()))
                .andExpect(jsonPath("$[0].legalFileTeamMembers[0].username").value(fileCreator1.getUsername()))
                .andExpect(jsonPath("$[1].id").value(legalFile2.getId()))
                .andExpect(jsonPath("$[1].legalFileTeamMembers[0].username").value(fileCreator1.getUsername()));
    }

    @Test
    void shouldReturnClientLegalFile() {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(usernameClient);

        UserProfile fileCreator1 = new UserProfile();
        fileCreator1.setUsername(usernameProfessional);
        fileCreator1.setProfession(Profession.AVOCAT);

        when(userProfileService.findByUsername(usernameProfessional)).thenReturn(fileCreator1);

        LegalFile legalFile1 = new LegalFile();
        legalFile1.setId("LOG-BOUCBOBB-0001");
        legalFile1.setFirstName("Bobby");
        legalFile1.setLastName("Boucher");
        legalFile1.setEmail(usernameClient);
        legalFile1.setPhoneNumber( "4505551234");
        legalFile1.setCivicNumber("123");
        legalFile1.setStreetName("St-Hubert");
        legalFile1.setCity("Montreal");
        legalFile1.setProvince("QC");
        legalFile1.setPostalCode("H0H0H0");
        legalFile1.setBirthdate(LocalDate.parse("1970-01-01"));
        legalFile1.setStartOfMandate(LocalDate.parse("2025-03-28"));
        legalFile1.setLawField(LawField.LOGEMENT);
        legalFile1.setAgreement(PaymentAgreement.HORAIRE);
        legalFile1.setLegalFileTeamMembers(List.of(fileCreator1));

        when(legalFileService.findById(legalFile1.getId())).thenReturn(legalFile1);

        // Call method
        ResponseEntity<?> response = legalFileController.getClientLegalFile(legalFile1.getId(), authentication);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(legalFile1, response.getBody());
    }

    @Test
    void shouldNotReturnClientLegalFile() {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("otherProfessional@avocat.com");

        UserProfile fileCreator1 = new UserProfile();
        fileCreator1.setUsername(usernameProfessional);
        fileCreator1.setProfession(Profession.AVOCAT);

        when(userProfileService.findByUsername(usernameProfessional)).thenReturn(fileCreator1);

        LegalFile legalFile1 = new LegalFile();
        legalFile1.setId("LOG-BOUCBOBB-0001");
        legalFile1.setFirstName("Bobby");
        legalFile1.setLastName("Boucher");
        legalFile1.setEmail(usernameClient);
        legalFile1.setPhoneNumber( "4505551234");
        legalFile1.setCivicNumber("123");
        legalFile1.setStreetName("St-Hubert");
        legalFile1.setCity("Montreal");
        legalFile1.setProvince("QC");
        legalFile1.setPostalCode("H0H0H0");
        legalFile1.setBirthdate(LocalDate.parse("1970-01-01"));
        legalFile1.setStartOfMandate(LocalDate.parse("2025-03-28"));
        legalFile1.setLawField(LawField.LOGEMENT);
        legalFile1.setAgreement(PaymentAgreement.HORAIRE);
        legalFile1.setLegalFileTeamMembers(List.of(fileCreator1));


        when(legalFileService.findById(legalFile1.getId())).thenReturn(legalFile1);

        // Call method
        ResponseEntity<?> response = legalFileController.getClientLegalFile(legalFile1.getId(), authentication);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Vous n'avez pas accès à ce dossier", response.getBody());

    }

    @Test
    void shouldReturnProfessionalLegalFile() {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(usernameProfessional);

        UserProfile fileCreator1 = new UserProfile();
        fileCreator1.setUsername(usernameProfessional);
        fileCreator1.setProfession(Profession.AVOCAT);

        when(userProfileService.findByUsername(usernameProfessional)).thenReturn(fileCreator1);

        LegalFile legalFile1 = new LegalFile();
        legalFile1.setId("LOG-BOUCBOBB-0001");
        legalFile1.setFirstName("Bobby");
        legalFile1.setLastName("Boucher");
        legalFile1.setEmail(usernameClient);
        legalFile1.setPhoneNumber( "4505551234");
        legalFile1.setCivicNumber("123");
        legalFile1.setStreetName("St-Hubert");
        legalFile1.setCity("Montreal");
        legalFile1.setProvince("QC");
        legalFile1.setPostalCode("H0H0H0");
        legalFile1.setBirthdate(LocalDate.parse("1970-01-01"));
        legalFile1.setStartOfMandate(LocalDate.parse("2025-03-28"));
        legalFile1.setLawField(LawField.LOGEMENT);
        legalFile1.setAgreement(PaymentAgreement.HORAIRE);
        legalFile1.setLegalFileTeamMembers(List.of(fileCreator1));


        when(legalFileService.findById(legalFile1.getId())).thenReturn(legalFile1);

        ResponseEntity<?> response = legalFileController.getProfessionnelLegalFile(legalFile1.getId(), authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(legalFile1, response.getBody());
    }

    @Test
    void shouldNotReturnProfessionalLegalFile(){

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("otherProfessional@avocat.com");

        UserProfile fileCreator1 = new UserProfile();
        fileCreator1.setUsername(usernameProfessional);
        fileCreator1.setProfession(Profession.AVOCAT);

        when(userProfileService.findByUsername(usernameProfessional)).thenReturn(fileCreator1);

        LegalFile legalFile1 = new LegalFile();
        legalFile1.setId("LOG-BOUCBOBB-0001");
        legalFile1.setFirstName("Bobby");
        legalFile1.setLastName("Boucher");
        legalFile1.setEmail(usernameClient);
        legalFile1.setPhoneNumber( "4505551234");
        legalFile1.setCivicNumber("123");
        legalFile1.setStreetName("St-Hubert");
        legalFile1.setCity("Montreal");
        legalFile1.setProvince("QC");
        legalFile1.setPostalCode("H0H0H0");
        legalFile1.setBirthdate(LocalDate.parse("1970-01-01"));
        legalFile1.setStartOfMandate(LocalDate.parse("2025-03-28"));
        legalFile1.setLawField(LawField.LOGEMENT);
        legalFile1.setAgreement(PaymentAgreement.HORAIRE);
        legalFile1.setLegalFileTeamMembers(List.of(fileCreator1));


        when(legalFileService.findById(legalFile1.getId())).thenReturn(legalFile1);

        ResponseEntity<?> response = legalFileController.getProfessionnelLegalFile(legalFile1.getId(), authentication);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Vous n'avez pas accès à ce dossier", response.getBody());
    }
}