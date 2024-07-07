package com.hng.stagetwo.jwt.service;

import com.hng.stagetwo.serviceImpl.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
@Service
public class JwtTokenService {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Value("${secret.key}")
    public String secretKey;
    @Value("${jwt.expiration}")
    public long validityInMilliseconds;

    public String createToken(String email, List<String> roles){
        Claims claims = Jwts.claims();
        claims.setSubject(email);
        claims.put("roles", roles);
        Date issuedDate = new Date();
        Date expiresAt = new Date(issuedDate.getTime() +validityInMilliseconds);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedDate)
                .setExpiration(expiresAt)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }
    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    public boolean isTokenNotExpired(String token){
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}