package com.backend.suits.service;

import com.backend.suits.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Déterminer où stocker la clé secrète
    private final String SECRET_KEY = "9e1fe3fbce4228746c8ab48b671def3ac686e2775f37987ecc50dc37fc9ee0fe";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(User user) {

        long nowInMillis = System.currentTimeMillis();
        int jwtExpirationDelayInMillis = 1000 * 60 * 60;

        return Jwts.builder()
                .subject(user.getUsername()) //.setSubject(user.getUsername())
                .claim("role", "ROLE_" + user.getRole().name())
                .issuedAt(new Date(nowInMillis))
                .expiration(new Date(nowInMillis + jwtExpirationDelayInMillis))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, User user) {
        return extractUsername(token).equals(user.getUsername());
    }
}
