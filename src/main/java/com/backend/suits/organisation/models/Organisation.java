package com.backend.suits.organisation.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Organisation {

    final static ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    @Size(max=100, message = "Le nom d'une organisation ne peut pas avoir plus de 100 caractères")
    private String name;

    @Size(max=255, message = "La description courte d'une organisation ne peut pas avoir plus de 255 caractères")
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String longDescription;

    @Column(length = 10)
    @Pattern(regexp = "^[0-9]{10}|[0-9]{0}$", message = "Le NEQ d'une organisation est composé de 10 nombres et ne peut pas être négatif")
    private String neq;

    @Column(length = 20)
    @Size(max=20, message = "Le numéros de taxes GST d'une organisation ne peut pas avoir plus de 20 caractères")
    private String taxGSTId;

    @Column(length = 20)
    @Size(max=20, message = "Le numéros de taxes QST d'une organisation ne peut pas avoir plus de 20 caractères")
    private String taxQSTId;

    @Pattern(regexp = "[a-zA-Z0-9 \\-àâçéèêëôûùüñÀÂÇÉÈÊËÎÏÔÛÙÜ]{0,255}", message = "L'adresse d'une organisation ne peut pas avoir plus de 255 caractères alphanumériques")
    private String address;

    @Pattern(regexp = "^\\D{0,255}$", message = "Le nom de ville d'une organisation ne peut pas avoir plus de 255 caractères alphabétiques")
    private String city;

    @Column(length = 100)
    @Pattern(regexp = "^\\D{0,100}$", message = "Le pays d'une organisation ne peut pas avoir plus de 100 caractères alphabétiques")
    private String country;

    @Column(length = 7)
    @Pattern(regexp = "^[A-Za-z][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]|[0-9]{0}$", message = "Le code postal d'une organisation doit respecter le format A1A 1A1")
    private String postalCode;

    @Column(length = 20)
    @Pattern(regexp = "^\\D{0,20}$", message="La province d'une organisation ne peut pas avoir plus de 20 caractères alphabétiques")
    private String province;

    @Column(length = 12)
    @Pattern(regexp = "^\\d{3}[- ]?\\d{3}[- ]?\\d{4}|\\d{0}$", message = "Le numéros de téléphone d'une organisation doit respecter le format XXX-XXX-XXXX")
    private String phone;

    @Email(message = "La valeur fournit ne correspond pas au format d'un courriel")
    private String email;

    @ElementCollection
    private List<TeamMembers> teamMembers;


    public static Organisation from_json(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Organisation.class);
    }
}
