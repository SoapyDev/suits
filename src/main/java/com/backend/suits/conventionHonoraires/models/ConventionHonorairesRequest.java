package com.backend.suits.conventionHonoraires.models;

import com.backend.suits.legalFile.entities.LegalFile;
import com.backend.suits.legalFile.entities.PaymentAgreement;


public record ConventionHonorairesRequest(String legalFileId, PaymentAgreement agreement,
                                          String mandat, int tarifHoraire, int estimationPrix) {
}
