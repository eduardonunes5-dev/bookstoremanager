package com.eduardonunes.bookstoremanager.users.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenManager {

    private Long jwtTokenValidity;
    private String secret;

    public JwtTokenManager(@Value("${jwt.validity}") Long jwtTokenValidity,
                           @Value("${jwt.secret}") String secret) {
        this.jwtTokenValidity = jwtTokenValidity;
        this.secret = secret;
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();

        return doGenerateToken(userDetails, claims);

    }

    public String getUsernameFromToken(String token){
        return getClaimForToken(token, Claims::getSubject);
    }

    public Date getExpirationFromTokan(String token){
        return getClaimForToken(token, Claims::getExpiration);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    public <T> T getClaimForToken(String token, Function<Claims,T> claimsResolver){
        Claims claims = getClaimsForToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getClaimsForToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    private String doGenerateToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity +  1000))
                .signWith(SignatureAlgorithm.HS512,secret).compact();
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationFromTokan(token);
        return expirationDate.before(new Date());
    }

}