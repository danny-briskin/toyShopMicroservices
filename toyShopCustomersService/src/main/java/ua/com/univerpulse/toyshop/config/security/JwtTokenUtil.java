package ua.com.univerpulse.toyshop.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@Component
@Log4j2
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -25502165626007488L;
    @Value("${jwt.tokenLive:#{24*60*60}}")
    private int tokenLive;

    @Value("${jwt.secretKey:JwtSecretKey}")
    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, @NotNull Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    @SuppressWarnings("unchecked")
    public UsernamePasswordAuthenticationToken getUserWithAuthorities(String token) {
        final Claims claims = this.getAllClaimsFromToken(token);
        String username = claims.getSubject();
        List<String> authorities = (List<String>) claims.get("authorities");
        return new UsernamePasswordAuthenticationToken(
                username, null, authorities.stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        log.warn("Token expiration [" + expiration + "]. Now is [" + new Date() + "]");
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(@NotNull UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + tokenLive * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
    }

    public boolean validateToken(String token, @NotNull UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}