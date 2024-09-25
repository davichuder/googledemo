package com.der.googledemo.config;

import java.util.Date;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.der.googledemo.entity.User;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.durationToken}")
    private Long durationToken;

    public String generateToken(User user) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Instant now = Instant.now();
        JwtBuilder jwtBuilder = Jwts.builder();
        String email = user.getEmail();
        String openId = user.getOpenId();
        if (email != null) {
            jwtBuilder.claim("email", email);
        }
        if (openId != null) {
            jwtBuilder.claim("openId", openId);
        }
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(now.plusMillis(durationToken)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    public String extractOpenId(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("openId", String.class);
    }

    public boolean validateToken(String token, User user) {
        String username = extractEmail(token);
        String openId = extractOpenId(token);
        return (username.equals(user.getUsername()) || openId.equals(user.getOpenId())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
