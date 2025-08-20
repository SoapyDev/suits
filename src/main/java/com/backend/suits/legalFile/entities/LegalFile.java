package com.backend.suits.legalFile.entities;

import com.backend.suits.document.models.Document;
import com.backend.suits.tasks.models.Status;
import com.backend.suits.userProfile.models.UserProfile;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Table
public class LegalFile {

    @Id
    private String id;

    @Column(nullable = false)
    @NonNull
    private String firstName;

    @Column(nullable = false)
    @NonNull
    private String lastName;

    @Column(nullable = false)
    @NonNull
    private String email;

    @Column(nullable = false)
    @NonNull
    private String phoneNumber;

    @Column(nullable = false)
    @NonNull
    private String civicNumber;

    @Column(nullable = false)
    @NonNull
    private String streetName;

    @Column(nullable = false)
    @NonNull
    private String city;

    @Column(nullable = false)
    @NonNull
    private String province;

    @Column(nullable = false)
    @NonNull
    private String postalCode;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NonNull
    private LocalDate birthdate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NonNull
    private LocalDate startOfMandate;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endOfMandate;

    @Column
    private boolean isClosed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private LawField lawField;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private PaymentAgreement agreement;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NON_DEBUTE;

    @Column(columnDefinition = "TEXT")
    private String description = "";

    @ManyToMany
    @JoinTable(
            name = "legal_file_team_members",
            joinColumns = @JoinColumn(name = "legal_file_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserProfile> legalFileTeamMembers;

    @OneToMany(mappedBy = "legalFile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Document> documents = new ArrayList<>();
}
