package com.backend.suits.document.models;

import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.userProfile.models.UserProfile;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private String storagePath;
    private Long fileSize;
    private String contentType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate uploadedDate;

    @ManyToOne
    @JoinColumn
    private UserProfile uploadedBy;

    @ManyToOne
    @JoinColumn
    @NotNull
    @JsonBackReference
    private LegalFile legalFile;

    @Transient
    private String signedUrl;

}
