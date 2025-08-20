package com.backend.suits.userProfile.services;

import com.backend.suits.entity.Role;
import com.backend.suits.entity.User;
import com.backend.suits.service.UserService;
import com.backend.suits.userProfile.models.Profession;
import com.backend.suits.userProfile.models.UserProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase?autoReconnect=true")
public class TestUserProfileService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User("username@gmail.com","Password!123","Paul", Role.PROFESSIONNEL);

        if (userService.findByUsername(user.getUsername()).isEmpty()) {
            userService.register(user);
        }
    }

    @AfterEach
    void tearDown() {

        if (userService.findByUsername(user.getUsername()).isEmpty()) return;

        UserProfile userProfile = userProfileService.findByUsername(user.getUsername());
        userProfile.setName("Paul");
        userProfile.setProfession(null);
        userProfile.setAddress(null);
        userProfile.setCity(null);
        userProfile.setPostalCode(null);
        userProfile.setBirthdate(null);
        userProfile.setPhoneNumber(null);

        userProfileService.updateUserProfile(userProfile);

    }


    @Test
    void testGetUserProfile() {
        UserProfile userProfile = userProfileService.findByUsername(user.getUsername());
        assertEquals(user.getUsername(), userProfile.getUsername());
        assertEquals(user.getName(), userProfile.getName());
    }

    @Test
    void testGetUserProfileNotFound() {
        UserProfile userProfile = userProfileService.findByUsername("notFound");
        assertNull(userProfile);
    }

    @Test
    void testUpdateUserProfile(){
        UserProfile userProfile = userProfileService.findByUsername(user.getUsername());
        userProfile.setName("Paul Bédard");
        userProfile.setProfession(Profession.JUGE);
        userProfile.setAddress("123 Main St");
        userProfile.setCity("Montreal");
        userProfile.setPostalCode("A1A 1A1");
        userProfile.setBirthdate(LocalDate.parse("2000-01-01"));
        userProfile.setPhoneNumber("4505551234");

        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userProfile);
        assertEquals("Paul Bédard", updatedUserProfile.getName());
        assertEquals(Profession.JUGE, updatedUserProfile.getProfession());
        assertEquals("123 Main St", updatedUserProfile.getAddress());
        assertEquals("Montreal", updatedUserProfile.getCity());
        assertEquals("A1A 1A1", updatedUserProfile.getPostalCode());
        assertEquals(LocalDate.parse("2000-01-01"), updatedUserProfile.getBirthdate());
        assertEquals("4505551234", updatedUserProfile.getPhoneNumber());
    }

}
