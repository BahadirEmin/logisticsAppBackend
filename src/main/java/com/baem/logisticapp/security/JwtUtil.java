package com.baem.logisticapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "5273cb88c1f40f2d52c43189db3ae95c9e2ac57df823768f3c71e64fe08dca419a8223ac8c864a7ffdeedbd3ee59a8ba035b101d008bbef226d38c334665455597cc5aca8dfd494b96c5432e6b9ea2a3ac4d38700f562cecfcbef8f1b8df002be3f66a0cdad8882dcdb17ae27f783c188bc2ad4ffb85030f9e07c1646d86e2dfc75af68e778f325641d6c705078c2f2f021dd9d0e046f6cc8a93cfdd9eb148bf1bb4d4867f7b1a394a9594a61be877aaf773fe1c83ebfa4e9026797e02b9351796dd758374a2b77d097b7220cda5b1bf32693896576af4a0031dd2c3bd7fe8b8bb2c59942c04dc551a0aa61ecd237f608835a47a1039bb7b41697ea8787c79e9";

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        // 24 hours
        long EXPIRATION_TIME = 1000 * 60 * 60 * 24;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}