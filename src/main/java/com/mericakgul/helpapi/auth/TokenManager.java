package com.mericakgul.helpapi.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Component
public class TokenManager {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expire.minutes}")
    private Integer expireMinutes;

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (expireMinutes * 60 * 1000)))
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }
// Note: Each of setSubject, setIssuedAt, setExpiration, etc are a claim. When I parse the token below I can reach each of the Claims

    private Claims parseToken(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(this.secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "The token that was sent is not a token signed by this server or expired.");
        }
    }

    public boolean hasTokenNotExpire(String token){
        Claims claims = this.parseToken(token);
        Date now = new Date(System.currentTimeMillis());
        return claims.getExpiration().after(now);
    }

    public String getUsernameFromToken(String token){
        Claims claims = this.parseToken(token);
        return claims.getSubject();
    }

    public boolean hasTokenValid(String token) {
        return getUsernameFromToken(token) != null && hasTokenNotExpire(token);
    }




}
