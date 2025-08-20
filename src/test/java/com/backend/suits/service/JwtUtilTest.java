package com.backend.suits.service;

import com.backend.suits.entity.Role;
import com.backend.suits.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    private User user;
    private SecretKey SECRET_KEY = Keys.hmacShaKeyFor("9e1fe3fbce4228746c8ab48b671def3ac686e2775f37987ecc50dc37fc9ee0fe".getBytes());
    private String testToken;
    Date issuedDate = new Date(System.currentTimeMillis());
    Date expirationDate = new Date(issuedDate.getTime() + (1000*60*60));

    @BeforeEach
    void setUp() {

        user = new User("username@gmail.com","Password!123","Paul Bédard", Role.CLIENT);
        testToken = Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(issuedDate)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY, Jwts.SIG.HS256)
                .compact();
    }

    @Test
    void generateToken() {
        String generatedToken = jwtUtil.generateToken(user);
        assertNotNull(generatedToken);
    }

    @Test
    void extractUsername() {

        String username = jwtUtil.extractUsername(testToken);
        assertNotNull(username);
        assertEquals(user.getUsername(), username);
    }

    @Test
    void extractClaim() {

        String subjectExtract = jwtUtil.extractClaim(testToken, Claims::getSubject);
        assertNotNull(subjectExtract);
        assertEquals(user.getUsername(), subjectExtract);

        Date issuedDateExtract = jwtUtil.extractClaim(testToken, Claims::getIssuedAt);
        assertNotNull(issuedDateExtract);

        Date expirationDateExtract = jwtUtil.extractClaim(testToken, Claims::getExpiration);
        assertNotNull(expirationDateExtract);

        String role = jwtUtil.extractClaim(testToken, claims -> claims.get("role", String.class));
        assertNotNull(role);
        assertEquals(Role.CLIENT, Role.valueOf(role));

    }

    @Test
    void validateToken() {
        assertTrue(jwtUtil.validateToken(testToken,user));

        StringBuilder tamperedToken = new StringBuilder(testToken).replace(29,35,"hterdf");
        System.out.println(testToken);
        System.out.println(tamperedToken);
        assertThrows(JwtException.class, () -> jwtUtil.validateToken(tamperedToken.toString(), user));
    }
}