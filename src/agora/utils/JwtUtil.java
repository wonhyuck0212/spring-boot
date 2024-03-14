package com.elice.agora.utils;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.elice.agora.user.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey
    
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);

    }

    /**
     * Access Token 생성
     * @param member
     * @return Access Token String
     */
    public String createAccessToken(UserEntity member) {
        return createToken(member);
    }


    /**
     * JWT 생성
     * @param member
     * @param expireTime
     * @return JWT String
     */
    private String createToken(UserEntity member) {
        Claims claims = Jwts.claims();
        claims.put("memberId", member.getEmail());

        ZonedDateTime now = ZonedDateTime.now();
 


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    // /**
    //  * Token에서 User ID 추출
    //  * @param token
    //  * @return User ID
    //  */
    // public String getUserId(String token) {
    //     return parseClaims(token).get("memberId", String.class);
    // }


    // /**
    //  * JWT 검증
    //  * @param token
    //  * @return IsValidate
    //  */
    // public boolean validateToken(String token) {
    //     try {
    //         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    //         return true;
    //     } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
    //         log.info("Invalid JWT Token", e);
    //     } catch (ExpiredJwtException e) {
    //         log.info("Expired JWT Token", e);
    //     } catch (UnsupportedJwtException e) {
    //         log.info("Unsupported JWT Token", e);
    //     } catch (IllegalArgumentException e) {
    //         log.info("JWT claims string is empty.", e);
    //     }
    //     return false;
    // }


    // /**
    //  * JWT Claims 추출
    //  * @param accessToken
    //  * @return JWT Claims
    //  */
    // public Claims parseClaims(String accessToken) {
    //     try {
    //         return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    //     } catch (ExpiredJwtException e) {
    //         return e.getClaims();
    //     }
    // }


}