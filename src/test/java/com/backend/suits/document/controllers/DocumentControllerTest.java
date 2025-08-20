package com.backend.suits.document.controllers;

import com.backend.suits.document.services.SupabaseStorageService;
import com.backend.suits.legalFile.services.LegalFileService;
import com.backend.suits.userProfile.models.UserProfile;
import com.backend.suits.userProfile.services.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DocumentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SupabaseStorageService storageService;

    @Mock
    private UserProfileService userProfileService;

    @Mock
    private LegalFileService legalFileService;

    @InjectMocks
    private DocumentController documentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();
    }

    @Test
    void testUploadDocument() throws Exception {

        String username = "test@gmail.com";
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_PROFESSIONNEL"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserProfile user = new UserProfile();
        user.setUsername("test@gmail.com");

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.pdf");

        when(userProfileService.findByUsername("test@gmail.com")).thenReturn(user);
        doNothing().when(storageService).uploadFile(anyString(), eq(file), anyString(), eq(user));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/dossier/1234/documents")
                .file("file", "test content".getBytes())
                .param("fileDescription", "Test file description")
                .principal(authentication)
        );

        result.andExpect(status().isOk());
        result.andExpect(content().string("Document téléversé !"));
    }
}
