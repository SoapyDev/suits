package com.backend.suits.conventionHonoraires.entities;

import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.legalFile.entities.PaymentAgreement;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
@Table(name="convention_honoraires")
public class ConventionHonoraires {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @OneToOne( cascade = CascadeType.ALL)
    LegalFile legalFile;

    @Column(nullable = false)
    PaymentAgreement agreement;

    @Column(nullable = false)
    LocalDate dateEntente;

    @Column(nullable = false)
    String mandat;

    @Column(nullable = false)
    int tarifHoraire;

    @Column(nullable = false)
    int estimationPrix;

    @Column(nullable = false)
    String creator;

    @Column
    Boolean isApproved;

    @Column
    LocalDate approvalDate;

}
