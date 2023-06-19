package com.alexpages.ebankingapi.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    //TODO to put it in application.yml
    private static final String SECRET_KEY = "asM6TPEUqKKZUwm/NcFRDxSCERnzpZQDf4chT4zJhgpNDDxdcB06fva9e8rwpUtl";

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
        //Subject is the clientId but for naming convention with spring boot we use Username
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails clientDetails){
        return generateToken(new HashMap<>(), clientDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails clientDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(clientDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24)) //expiration can be modified
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails clientDetails){
        final String username = extractUsername(token);
        return (username.equals(clientDetails.getUsername())&& !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes); //sha is the algorithm

    }
}
