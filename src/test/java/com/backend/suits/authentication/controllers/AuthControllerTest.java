package com.backend.suits.authentication.controllers;

import com.backend.suits.authentication.model.LoginRequest;
import com.backend.suits.authentication.model.RegisterRequest;
import com.backend.suits.entity.Role;
import com.backend.suits.entity.User;
import com.backend.suits.repository.UserRepository;
import com.backend.suits.service.JwtUtil;
import com.backend.suits.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager mockAuthenticationManager;
    @MockitoBean
    private JwtUtil mockJwtUtil;
    @MockitoBean
    private UserService mockUserService;
    @MockitoBean
    private UserRepository mockUserRepository;

    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginExistingUserAccountWithCorrectPassword()
            throws Exception {

        String username = "username@gmail.com";
        LoginRequest loginRequest = new LoginRequest(username, "Password!123");
        String hashedPassword = "hashedPassword";
        User user = new User(username,hashedPassword,"Name", Role.CLIENT);


        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        when(mockAuthenticationManager.authenticate(any())).thenReturn(authentication);
        when(mockUserRepository.findByUsername(loginRequest.username()))
                .thenReturn(Optional.of(user));
        when(mockUserService.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(mockJwtUtil.generateToken(any())).thenReturn("mockedToken");


        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, Matchers.containsString("SUITS_Token=mockedToken")))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.role").value("CLIENT"));

    }

    @Test
    void loginUserAccountWithIncorrectUsernameFormat()
            throws Exception {

        String username = "username";
        LoginRequest loginRequest = new LoginRequest(username, "Password!123");
        String hashedPassword = "hashedPassword";
        User user = new User(username,hashedPassword,"Name", Role.CLIENT);


        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        when(mockAuthenticationManager.authenticate(any())).thenReturn(authentication);
        when(mockUserRepository.findByUsername(loginRequest.username()))
                .thenReturn(Optional.of(user));
        when(mockUserService.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(mockJwtUtil.generateToken(any())).thenReturn("mockedToken");


        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void loginUserAccountWithUsernameNull()
            throws Exception {

        String username = null;
        LoginRequest loginRequest = new LoginRequest(username, "Password!123");

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        when(mockAuthenticationManager.authenticate(any())).thenReturn(authentication);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void loginUserAccountWithPasswordNull()
            throws Exception {

        String username = "username@gmail.com";
        LoginRequest loginRequest = new LoginRequest(username, null);
        String hashedPassword = "hashedPassword";

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        when(mockAuthenticationManager.authenticate(any())).thenReturn(authentication);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void loginExistingUserAccountWithIncorrectPassword()
            throws Exception {

        String username = "username@gmail.com";
        LoginRequest loginRequest = new LoginRequest(username, "wrongPassword!123");
        String hashedPassword = "hashedPassword";
        User user = new User(username,hashedPassword,"Name", Role.CLIENT);


        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        when(mockAuthenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Erreur"));
        when(mockUserRepository.findByUsername(loginRequest.username()))
                .thenReturn(Optional.of(user));
        when(mockUserService.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(mockJwtUtil.generateToken(any())).thenReturn("mockedToken");


        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden());

    }

    @Test
    void loginUserAccountWithUserNotFound()
            throws Exception {

        String username = "username@gmail.com";
        LoginRequest loginRequest = new LoginRequest(username, "wrongPassword!123");
        String hashedPassword = "hashedPassword";
        User user = new User(username,hashedPassword,"Name", Role.CLIENT);


        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        when(mockAuthenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Erreur"));
        when(mockUserRepository.findByUsername(loginRequest.username()))
                .thenReturn(Optional.empty());
        when(mockUserService.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(mockJwtUtil.generateToken(any())).thenReturn("mockedToken");


        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden());

    }

    @Test
    void registerNewUser()
            throws Exception {

        String username = "username@gmail.com";
        RegisterRequest registerRequest = new RegisterRequest(username, "Password!123","Name");
        String hashedPassword = "hashedPassword";
        User newUser = new User(username,hashedPassword,"Name", Role.CLIENT);

        when(mockUserRepository.findByUsername(registerRequest.username()))
                .thenReturn(Optional.empty());

        when(mockUserService.register(any())).thenReturn(newUser);

        mockMvc.perform(post("/login/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.role").value("CLIENT"));

    }

    @Test
    void registerNewUserWithExistingUsername()
            throws Exception {

        String username = "username@gmail.com";
        RegisterRequest registerRequest = new RegisterRequest(username, "Password!123","Name");
        String hashedPassword = "hashedPassword";
        User newUser = new User(username,hashedPassword,"Name", Role.CLIENT);
        User existingUser = new User("username@gmail.com","password","Name", Role.CLIENT);

        when(mockUserRepository.findByUsername(registerRequest.username()))
                .thenReturn(Optional.of(existingUser));

        when(mockUserService.register(any())).thenReturn(newUser);

        mockMvc.perform(post("/login/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void registerNewUserWithNameNull()
            throws Exception {

        String username = "username@gmail.com";
        RegisterRequest registerRequest = new RegisterRequest(username, "Password!123",null);
        String hashedPassword = "hashedPassword";
        User newUser = new User(username,hashedPassword,"Name", Role.CLIENT);

        when(mockUserRepository.findByUsername(registerRequest.username()))
                .thenReturn(Optional.empty());

        when(mockUserService.register(any())).thenReturn(newUser);

        mockMvc.perform(post("/login/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

    }

}