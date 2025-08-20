package com.backend.suits.legalFile.models;

import com.backend.suits.legalFile.entities.LawField;
import com.backend.suits.legalFile.entities.PaymentAgreement;

import java.time.LocalDate;

public record LegalFileRequest(String firstName, String lastName, String email, String phoneNumber, String civicNumber,
                               String streetName, String city, String province, String postalCode, LocalDate birthDate,
                               LocalDate startOfMandate, LawField lawField, PaymentAgreement agreementType, String fileCreator) {
}
