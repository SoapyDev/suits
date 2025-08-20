package com.backend.suits.service;

import com.backend.suits.entity.Role;
import com.backend.suits.entity.User;
import com.backend.suits.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testLoadUserByUsername() {

        String username = "username@gmail.com";
        User user = new User(username,"hashedPassword","Paul", Role.CLIENT);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertNotNull(userDetails);
        assertEquals(username,userDetails.getUsername());
        assertEquals("hashedPassword",userDetails.getPassword());
        assertEquals("ROLE_"+Role.CLIENT,userDetails.getAuthorities().iterator().next().getAuthority());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENT")));
    }

    @Test
    void testLoadUserByUsernameNotFound() {

        String username = "username@gmail.com";
        String otherUsername = "usernameInexistant@gmail.com";
        User user = new User(username,"hashedPassword","Paul", Role.CLIENT);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(otherUsername);
        });
    }
}