package com.cibertec.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cibertec.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {
    
	private static final String SECRET_KEY =
            "Inventary_System_2026_Mi_Clave_Secreta_JWT_Cibertec";
    
    private final Key key =
            Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
            );

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());

        // 👇 AGREGAMOS ESTO
        claims.put("username", user.getUsername());

        return generateToken(claims, user.getEmail()); // 👈 sub = email (correcto)
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());

        // 👇 AQUÍ NO tienes username directo desde UserDetails
        claims.put("username", userDetails.getUsername());

        return generateToken(claims, userDetails.getUsername());
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)      // 👈 SIN "set"
                .subject(subject)    // 👈 SIN "set"
                .issuedAt(new Date()) // 👈 SIN "set"
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 👈 SIN "set"
                .signWith(key)
                .compact();
    }
    
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)  // 👈 USAR verifyWith
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean isTokenValid(String token, String username) {
        String tokenUsername = extractUsername(token);
        return tokenUsername.equals(username);
    }
}
