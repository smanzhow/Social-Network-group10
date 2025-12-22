package se.jensen.saman.socialnetworkmaven.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import se.jensen.saman.socialnetworkmaven.model.User;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // När den går ut
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signera med nyckeln
                .compact();
    }

    public String generateExpiredToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 2))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);


        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
