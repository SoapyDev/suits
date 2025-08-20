package com.backend.suits.userProfile.controllers;

import com.backend.suits.userProfile.models.UserProfile;
import com.backend.suits.userProfile.services.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/api/user/profile")
    public UserProfile getUserProfile(Authentication authentication) {
        String email = authentication.getName();
        return userProfileService.findByUsername(email);
    }

//    @GetMapping("/api/users")
//    public List<UserProfileDTO> getAllUsersDTO(Authentication authentication) {
//
//        return userProfileService.getAllUsersDTO();
//    }
    @GetMapping("/api/users")
    public List<UserProfile> getAllUsers() {
        return userProfileService.getAllUsers();
    }

    @PostMapping("/api/user/profile")
    public UserProfile saveUserProfile(@RequestBody UserProfile userProfile) {
        return userProfileService.updateUserProfile(userProfile);
    }

}
