package com.mylearning.jwt;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class JWTUtil {
    private static final String SECRETKEY = "bladimirgonzales bladimirgonzales bladimirgonzales bladimirgonzales";

    public String issueToken(
            String subject
    ){
        return issueToken(subject, Map.of());
    }

    public String issueToken(
            String subject,
            String ...scopes
    ){
        return issueToken(subject, Map.of("scopes",scopes));
    }

    public String issueToken(
            String subject,
            Map<String, Object> claims
    ){
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("https://mylearning.bladimir.com")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(
                        Date.from(
                                Instant.now().plus(13, ChronoUnit.DAYS)
                        )
                )
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    private Claims getClaims(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
    public String getSubject(String token){
        return getClaims(token).getSubject();
    }
    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRETKEY.getBytes());
    }

    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);
        return subject.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        Date today = Date.from(Instant.now());
        return getClaims(jwt).getExpiration().before(today);
    }
}
