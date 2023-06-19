package com.crimealert.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class SessionTokenUtils {
    public static String generateToken(String userId) {
        // Secure signing key
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Set the claims for the token
        Claims claims = Jwts.claims().setSubject(userId);

        // Generate the token with the claims and sign it with the key
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }
}
