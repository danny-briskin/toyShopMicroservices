package ua.com.univerpulse.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@Log4j2
public class JwtUsernameAndPasswordAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {
    public static String TOKEN;

    private RestTemplate restTemplate;
    private Claims claims;

    private final JwtConfig jwtConfig;

    public JwtUsernameAndPasswordAuthenticationFilter(@NotNull JwtConfig jwtConfig
            , RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public Authentication attemptAuthentication(@NotNull HttpServletRequest request
            , HttpServletResponse response) {
        if (this.claims == null || !this.isTokenValid()) {
            String username = obtainUsername(request);
            String password = obtainPassword(request);
            TOKEN = this.getToken(username, password);
            log.warn("Received [" + TOKEN + "]");
        }
        return this.verifyToken(TOKEN);
    }

    private void getTokenClaims(String token) {
        this.claims = Jwts.parser()
                .setSigningKey(jwtConfig.getSecret().getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenValid() {
        return !this.claims.getExpiration().before(new Date());
    }

    @Nullable
    private Authentication verifyToken(String token) {
        this.getTokenClaims(token);

        String username = this.claims.getSubject();
        if (username != null) {
            @SuppressWarnings("unchecked")
            List<String> authorities = (List<String>) this.claims.get("authorities");
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, authorities.stream()
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            return auth;
        } else {
            fail();
            return null;
        }
    }

    private String getToken(String username, String password) {
        /* Create the request body as a MultiValueMap*/
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<?> httpEntity = new HttpEntity<>(loginRequest, requestHeaders);

        ResponseEntity<LoginResponse> loginResponse = restTemplate
                .exchange("http://localhost:9100/auth"
                        , HttpMethod.POST, httpEntity, LoginResponse.class);
        assertEquals(200, loginResponse.getStatusCodeValue()
                , "Unsuccessful login. ");
        LoginResponse body = loginResponse.getBody();
        assertNotNull(body);
        return loginResponse.getBody().getJwtToken();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    private static class LoginResponse {
        private String jwtToken;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    static
    class LoginRequest {
        private String username;
        private String password;
    }
}