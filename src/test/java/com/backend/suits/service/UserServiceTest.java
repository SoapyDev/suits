package com.backend.suits.service;

import com.backend.suits.entity.Role;
import com.backend.suits.entity.User;
import com.backend.suits.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserRepository userRepository;

    private User userClient;
    private String usernameClient;
    private String passwordClient;
    private String usernameProfessional;
    private String passwordProfessional;
    private User userProfessional;

    @BeforeEach
    void setUp() {
        usernameClient = "username@gmail.com";
        passwordClient = "Password!123";
        userClient = new User(usernameClient,passwordClient,"Paul", Role.CLIENT);
        System.out.println(userClient.getPassword());

        usernameProfessional = "username@bigjob.com";
        passwordProfessional = "pAssWord!146";
        userProfessional = new User(usernameProfessional,passwordProfessional , "Tony", Role.PROFESSIONNEL);
        System.out.println(userProfessional.getPassword());
    }

    @Test
    void findByUsernameUserFound() {

        when(userRepository.findByUsername(usernameClient)).thenReturn(Optional.of(userClient));
        Optional<User> userFound = userService.findByUsername(usernameClient);
        assertTrue(userFound.isPresent());
        assertEquals(usernameClient, userFound.get().getUsername());
    }

    @Test
    void findByUsernameUserNotFound() {

        when(userRepository.findByUsername(usernameClient)).thenReturn(Optional.empty());
        Optional<User> userFound = userService.findByUsername(usernameClient);
        assertFalse(userFound.isPresent());
        assertTrue(userFound.isEmpty());
    }

    @Test
    void registerClient() {

        String password = userClient.getPassword();
        when(userRepository.findByUsername(usernameClient)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User registeredUser = userService.register(userClient);

        assertNotNull(registeredUser);
        assertEquals(usernameClient, registeredUser.getUsername());
        assertEquals(userClient.getRole(), registeredUser.getRole());
        assertEquals(userClient.getName(), registeredUser.getName());
        assertNotEquals(password, registeredUser.getPassword());
        assertTrue(passwordEncoder.matches(password, registeredUser.getPassword()));
    }

    @Test
    void registerProfessional() {

        String password = userProfessional.getPassword();
        when(userRepository.findByUsername(usernameProfessional)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User registeredProfessionalUser = userService.register(userProfessional);

        assertNotNull(registeredProfessionalUser);
        assertEquals(usernameProfessional, registeredProfessionalUser.getUsername());
        assertEquals(userProfessional.getRole(), registeredProfessionalUser.getRole());
        assertEquals(userProfessional.getName(), registeredProfessionalUser.getName());
        assertNotEquals(passwordProfessional, registeredProfessionalUser.getPassword());
        assertTrue(passwordEncoder.matches(passwordProfessional, registeredProfessionalUser.getPassword()));

    }
}