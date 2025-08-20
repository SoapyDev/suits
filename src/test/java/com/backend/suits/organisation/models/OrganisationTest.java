package com.backend.suits.organisation.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase?autoReconnect=true")
public class OrganisationTest {

    private Organisation organisation;
    private Validator validator;

    @BeforeEach
    void setUp(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        organisation = new Organisation();
    }

    @Test
    public void testOrganisationName() {
        String name = "test name";

        organisation.setName(name);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyOrganisationName() {
        String emptyName = "";

        organisation.setName(emptyName);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testLongOrganisationName(){
        String longName = "a".repeat(101);

        organisation.setName(longName);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le nom d'une organisation ne peut pas avoir plus de 100 caractères", violations.iterator().next().getMessage());
    }

    @Test
    public void testOrganisationShortDescription() {
        String shortDescription = "test short description";

        organisation.setShortDescription(shortDescription);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyOrganisationShortDescription() {
        String emptyShortDescription = "";

        organisation.setShortDescription(emptyShortDescription);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testLongOrganisationShortDescription(){
        String longShortDescription = "a".repeat(256);

        organisation.setShortDescription(longShortDescription);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("La description courte d'une organisation ne peut pas avoir plus de 255 caractères", violations.iterator().next().getMessage());
    }

    @Test
    public void testOrganisationLongDescription() {
        String longDescription = "test long description";

        organisation.setLongDescription(longDescription);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyOrganisationLongDescription() {
        String emptyLongDescription = "";

        organisation.setLongDescription(emptyLongDescription);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testNeq() {
        String neq = "1234567890";

        organisation.setNeq(neq);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyNeq() {
        String emptyNeq = "";

        organisation.setNeq(emptyNeq);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }



    @Test
    public void testNeqLength() {
        String invalidNeq = "12345678901";

        organisation.setNeq(invalidNeq);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le NEQ d'une organisation est composé de 10 nombres et ne peut pas être négatif", violations.iterator().next().getMessage());
    }

    @Test
    public void testNeqNegative() {
        String invalidNeq = "-123456789";

        organisation.setNeq(invalidNeq);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le NEQ d'une organisation est composé de 10 nombres et ne peut pas être négatif", violations.iterator().next().getMessage());
    }

    @Test
    public void testTaxGSTId() {
        String taxGSTId = "123456789";

        organisation.setTaxGSTId(taxGSTId);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyTaxGSTId() {
        String emptyTaxGSTId = "";

        organisation.setTaxGSTId(emptyTaxGSTId);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testTaxGSTIdLength() {
        String invalidTaxGSTId = "a".repeat(21);;

        organisation.setTaxGSTId(invalidTaxGSTId);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le numéros de taxes GST d'une organisation ne peut pas avoir plus de 20 caractères", violations.iterator().next().getMessage());
    }

    @Test
    public void testTaxQSTId() {
        String taxQSTId = "123456789";

        organisation.setTaxQSTId(taxQSTId);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyTaxQSTId() {
        String emptyTaxQSTId = "";

        organisation.setTaxQSTId(emptyTaxQSTId);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testTaxQSTIdLength() {
        String invalidTaxQSTId = "1".repeat(21);

        organisation.setTaxQSTId(invalidTaxQSTId);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le numéros de taxes QST d'une organisation ne peut pas avoir plus de 20 caractères", violations.iterator().next().getMessage());
    }

    @Test
    public void testAddress() {
        String address = "123 test-address";

        organisation.setAddress(address);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyAddress() {
        String emptyAddress = "";

        organisation.setAddress(emptyAddress);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testAddressLength() {
        String invalidAddress = "1".repeat(256);

        organisation.setAddress(invalidAddress);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("L'adresse d'une organisation ne peut pas avoir plus de 255 caractères alphanumériques", violations.iterator().next().getMessage());
    }

    @Test
    public void testCity() {
        String city = "test-city";

        organisation.setCity(city);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyCity() {
        String emptyCity = "";

        organisation.setCity(emptyCity);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testCityLength() {
        String invalidCity = "a".repeat(256);

        organisation.setCity(invalidCity);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le nom de ville d'une organisation ne peut pas avoir plus de 255 caractères alphabétiques", violations.iterator().next().getMessage());
    }

    @Test
    public void testCityAlphabetic() {
        String invalidCity = "123";

        organisation.setCity(invalidCity);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le nom de ville d'une organisation ne peut pas avoir plus de 255 caractères alphabétiques", violations.iterator().next().getMessage());
    }

    @Test
    public void testCountry() {
        String country = "test-country";

        organisation.setCountry(country);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyCountry() {
        String emptyCountry = "";

        organisation.setCountry(emptyCountry);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testCountryLength() {
        String invalidCountry = "a".repeat(101);

        organisation.setCountry(invalidCountry);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le pays d'une organisation ne peut pas avoir plus de 100 caractères alphabétiques", violations.iterator().next().getMessage());
    }

    @Test
    public void testCountryAlphabetic() {
        String invalidCountry = "123";

        organisation.setCountry(invalidCountry);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le pays d'une organisation ne peut pas avoir plus de 100 caractères alphabétiques", violations.iterator().next().getMessage());
    }

    @Test
    public void testPostalCode() {
        String postalCode = "A1A 1A1";

        organisation.setPostalCode(postalCode);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testPostalCodeWithoutSpace() {
        String postalCode = "A1A1A1";

        organisation.setPostalCode(postalCode);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyPostalCode() {
        String emptyPostalCode = "";

        organisation.setPostalCode(emptyPostalCode);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testPostalCodeTooShort() {
        String invalidPostalCode = "A1A 1A";

        organisation.setPostalCode(invalidPostalCode);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le code postal d'une organisation doit respecter le format A1A 1A1", violations.iterator().next().getMessage());
    }

    @Test
    public void testPostalCodeTooLong() {
        String invalidPostalCode = "A1A 1A1 1A1";

        organisation.setPostalCode(invalidPostalCode);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le code postal d'une organisation doit respecter le format A1A 1A1", violations.iterator().next().getMessage());
    }

    @Test
    public void testPostalCodeIncorrectFormat() {
        String invalidPostalCode = "1A1 A1A";

        organisation.setPostalCode(invalidPostalCode);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le code postal d'une organisation doit respecter le format A1A 1A1", violations.iterator().next().getMessage());
    }

    @Test
    public void testProvice() {
        String province = "Québec";

        organisation.setProvince(province);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyProvince() {
        String emptyProvince = "";

        organisation.setProvince(emptyProvince);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testProvinceLength() {
        String invalidProvince = "a".repeat(21);

        organisation.setProvince(invalidProvince);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("La province d'une organisation ne peut pas avoir plus de 20 caractères alphabétiques", violations.iterator().next().getMessage());
    }

    @Test
    public void testProvinceAlphabetic() {
        String invalidProvince = "123";

        organisation.setProvince(invalidProvince);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("La province d'une organisation ne peut pas avoir plus de 20 caractères alphabétiques", violations.iterator().next().getMessage());
    }

    @Test
    public void testPhoneWithoutSpace() {
        String phone = "1234567890";

        organisation.setPhone(phone);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testPhoneWithSpace() {
        String phone = "123 456 7890";

        organisation.setPhone(phone);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testPhoneWithDashes() {
        String phone = "123-456-7890";

        organisation.setPhone(phone);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyPhone() {
        String emptyPhone = "";

        organisation.setPhone(emptyPhone);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testPhoneTooLong() {
        String invalidPhone = "12345678901";

        organisation.setPhone(invalidPhone);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le numéros de téléphone d'une organisation doit respecter le format XXX-XXX-XXXX", violations.iterator().next().getMessage());
    }

    @Test
    public void testPhoneTooShort() {
        String invalidPhone = "123456789";

        organisation.setPhone(invalidPhone);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le numéros de téléphone d'une organisation doit respecter le format XXX-XXX-XXXX", violations.iterator().next().getMessage());
    }

    @Test
    public void testPhoneIncorrectFormat() {
        String invalidPhone = "123-456-78901";

        organisation.setPhone(invalidPhone);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le numéros de téléphone d'une organisation doit respecter le format XXX-XXX-XXXX", violations.iterator().next().getMessage());
    }

    @Test
    public void testPhoneAlphabetic() {
        String invalidPhone = "abc-abc-abcd";

        organisation.setPhone(invalidPhone);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("Le numéros de téléphone d'une organisation doit respecter le format XXX-XXX-XXXX", violations.iterator().next().getMessage());
    }


    @Test
    public void testEmail(){
        String email = "email@example.com";

        organisation.setEmail(email);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmptyEmail() {
        String emptyEmail = "";

        organisation.setEmail(emptyEmail);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmailFormat() {
        String invalidEmail = "email@example";

        organisation.setEmail(invalidEmail);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(0, violations.size());
    }

    @Test
    public void testEmailTooLong() {
        String invalidEmail = "e".repeat(100) + "@" + "m".repeat(154) + ".com";

        organisation.setEmail(invalidEmail);
        Set<ConstraintViolation<Organisation>> violations = validator.validate(organisation);

        assertEquals(1, violations.size());
        assertEquals("La valeur fournit ne correspond pas au format d'un courriel", violations.iterator().next().getMessage());
    }
}

