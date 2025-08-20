package com.backend.suits.userProfile.services;

import com.backend.suits.userProfile.models.UserProfile;
import com.backend.suits.userProfile.models.UserProfileDTO;
import com.backend.suits.userProfile.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile findByUsername(String username) {
        return userProfileRepository.findByUsername(username).orElse(null);
    }

    public List<UserProfileDTO> getAllUsersDTO() {

        List<UserProfile> userProfiles = userProfileRepository.findAll();

        return userProfiles.stream()
                .map(userProfile -> new UserProfileDTO(
                        userProfile.getUsername(),
                        userProfile.getName(),
                        userProfile.getProfession() != null ? userProfile.getProfession().name() : "",
                        ""))
                .collect(Collectors.toList());
    }

    public List<UserProfile> getAllUsers() {

        return userProfileRepository.findAll();
    }

    public UserProfile updateUserProfile(@Validated UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }
}
