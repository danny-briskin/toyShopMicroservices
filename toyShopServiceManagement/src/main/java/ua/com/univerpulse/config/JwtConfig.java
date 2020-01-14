package ua.com.univerpulse.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@Data
public class JwtConfig {
    @Value("${security.jwt.uri:/auth/**}")
    private String uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${jwt.tokenLive:#{24*60*60}}")
    private int expiration;

    @Value("${jwt.secretKey:JwtSecretKey}")
    private String secret;
}
