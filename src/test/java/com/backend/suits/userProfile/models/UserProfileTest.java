package com.backend.suits.userProfile.models;

import com.backend.suits.entity.Role;
import com.backend.suits.entity.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserProfileTest {
    private Validator validator;
    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        userProfile = new UserProfile("username@gmail.com","Paul Bédard");
    }

    @Test
    void testEmptyUserProfile(){
        User user = new User();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    void testEmptyUsername(){

        try{
            new User(null,"Password!123","Paul Bédard", Role.PROFESSIONNEL);
        }catch (Exception e){
            assertTrue(true);
            return;
        }
        fail("Username should not be null");
    }

    @Test
    void testProperUserProfile(){
        User user = new User("username@gmail.com","Password!123","Paul Bédard", Role.PROFESSIONNEL);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    void testUsernameNotEmail(){
        User user = new User("username","Password!123","Paul Bédard", Role.PROFESSIONNEL);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    void testNameTooShort(){
        userProfile.setName("A");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testNameTooLong(){
        userProfile.setName("A".repeat(76));
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testNameValid(){
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testBirthDateValid(){
        userProfile.setBirthdate(LocalDate.parse("2000-01-01"));
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testBirthDateTooOld(){
        userProfile.setBirthdate(LocalDate.parse("1899-01-01"));
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testBirthDateTooYoung(){
        userProfile.setBirthdate(LocalDate.now());
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testBirthDateInTheFuture(){
        userProfile.setBirthdate(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testAddressValid(){
        userProfile.setAddress("123 test-address");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testAddressTooLong(){
        userProfile.setAddress("1".repeat(256));
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testPhoneNumberValid(){
        userProfile.setPhoneNumber("1234567890");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testPhoneNumberEmpty(){
        userProfile.setPhoneNumber("");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testPhoneContainsDashes(){
        userProfile.setPhoneNumber("123-456-7890");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testPhoneContainsSpaces(){
        userProfile.setPhoneNumber("123 456 7890");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testPhoneContainsParentheses(){
        userProfile.setPhoneNumber("(123) 456-7890");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testPhoneNumberTooLong() {
        userProfile.setPhoneNumber("1".repeat(13));
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testPhoneNumberTooShort() {
        userProfile.setPhoneNumber("1".repeat(9));
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testPhoneNumberInvalid() {
        userProfile.setPhoneNumber("1".repeat(12));
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testCityValid(){
        userProfile.setCity("test-city");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testCityTooLong(){
        userProfile.setCity("1".repeat(101));
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testCityEmpty(){
        userProfile.setCity("");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testPostalCodeValid(){
        userProfile.setPostalCode("A1A 1A1");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testPostalCodeNoSpaces(){
        userProfile.setPostalCode("A1A1A1");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testPostalCodeEmpty(){
        userProfile.setPostalCode("");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testPostalCodeTooLong(){
        userProfile.setPostalCode("A1A 1A1 1A1");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(2, violations.size());
    }

    @Test
    void testPostalCodeTooShort(){
        userProfile.setPostalCode("A1A");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testPostalCodeInvalid(){
        userProfile.setPostalCode("A1A 1AA");
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(1, violations.size());
    }

    @Test
    void testProfessionValid(){
        userProfile.setProfession(Profession.AVOCAT);
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

    @Test
    void testProfessionNull(){
        userProfile.setProfession(null);
        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
        assertEquals(0, violations.size());
    }

}
