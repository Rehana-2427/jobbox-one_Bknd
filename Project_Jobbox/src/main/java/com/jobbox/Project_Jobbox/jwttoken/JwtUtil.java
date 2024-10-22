package com.jobbox.Project_Jobbox.jwttoken;

import java.util.Date;

import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	
	// Get the dynamically generated secret key
    private final String SECRET_KEY = SecretKeyGeneratorExample.generateSecretKey();
    
    private final int EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    // Method to generate a JWT token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // Use the generated secret key here
                .compact();
    }

    // Method to validate a JWT token
    public boolean validateToken(String token, String email) {
        final String username = extractUsername(token);
        return (username.equals(email) && !isTokenExpired(token));
    }

    // Method to extract the username (email) from a token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Method to extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)  // Use the same generated secret key for validation
                .parseClaimsJws(token)
                .getBody();
    }

    // Method to check if a token is expired
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
