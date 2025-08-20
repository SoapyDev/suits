package com.backend.suits.userProfile.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class UserProfile {

    public UserProfile(@NonNull String username, String name) {
        this.username = username;
        this.name = name;
    }

    @Id
    @Email
    @Column(nullable = false, unique = true)
    @NonNull
    private String username;

    @Size(min=2, max=75, message = "Le nom doit avoir entre 2 et 75 caractères")
    @Column(length = 75)
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "La date de naissance doit etre dans le passe")
    private LocalDate birthdate;

    @Size(max=255, message = "L'adresse ne peut pas avoir plus de 255 caractères")
    private String address;

    @Size(max=20, message = "Le numéros de téléphone ne peut pas avoir plus de 20 caractères")
    @Column(length = 20)
    @Pattern(regexp = "^\\d{3}[- ]?\\d{3}[- ]?\\d{4}|\\d{0}$", message = "Le numéros de téléphone d'une organisation doit respecter le format XXX-XXX-XXXX")
    private String phoneNumber;

    @Column(length = 100)
    @Size(max=100, message = "Le nom de ville ne peut pas avoir plus de 100 caractères")
    private String city;

    @Column(length = 7)
    @Size(max=7, message = "Le code postal ne peut pas avoir plus de 7 caractères")
    @Pattern(regexp = "^[A-Za-z][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]|[0-9]{0}$", message = "Le code postal d'une organisation doit respecter le format A1A 1A1")
    private String postalCode;

    private final String province = "Québec";

    private final String country = "Canada";

    @Enumerated(EnumType.STRING)
    private Profession profession;

    @Column(columnDefinition = "TEXT")
    private String profilePicturePath;

    @AssertTrue
    public boolean isValidBirthdate(){
        return birthdate == null || birthdate.isAfter(LocalDate.parse("1900-01-01"));
    }
}
