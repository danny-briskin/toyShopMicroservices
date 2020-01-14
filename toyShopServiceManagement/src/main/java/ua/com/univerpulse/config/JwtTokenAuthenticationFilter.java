package ua.com.univerpulse.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@Log4j2
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    //    private static String token = null;
    private RestTemplate restTemplate;
    private BCryptPasswordEncoder passwordEncoder;

    private final JwtConfig jwtConfig;

    public JwtTokenAuthenticationFilter(JwtConfig jwtConfig, RestTemplate restTemplate
            , BCryptPasswordEncoder passwordEncoder) {
        this.jwtConfig = jwtConfig;
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request
            , HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token;
        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
        String header = request.getHeader(jwtConfig.getHeader());

        // 2. validate the header and check the prefix
//        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {

//            if (token == null) {
//                token = getToken();
//                log.warn("[" + token + "]");
//            }
//
//            chain.doFilter(request, response);  		// If not valid, go to the next filter.
//            return;
//        }
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            token = getToken();
        } else {
            if (header != null && header.startsWith(jwtConfig.getPrefix())) {
                token = header.replace(jwtConfig.getPrefix(), "");
            } else {
                token ="";
                throw  new RuntimeException();
            }
        }

            // If there is no token provided and hence the user won't be authenticated.
            // It's Ok. Maybe the user accessing a public path or asking for a token.

            // All secured paths that needs a token are already defined and secured in config class.
            // And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.

            // 3. Get the token
//        String token = header.replace(jwtConfig.getPrefix(), "");

            try {    // exceptions might be thrown in creating the claims if for example the token is expired

                // 4. Validate the token
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtConfig.getSecret().getBytes())
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                if (username != null) {
                    @SuppressWarnings("unchecked")
                    List<String> authorities = (List<String>) claims.get("authorities");

                    // 5. Create auth object
                    // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                    // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username, null, authorities.stream()
                            .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                    // 6. Authenticate the user
                    // Now, user is authenticated
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception e) {
                log.catching(e);
                // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
                SecurityContextHolder.clearContext();
            }

        // go to the next filter in the filter chain
        chain.doFilter(request, response);
    }

    private String getToken() {

        /* Create the request body as a MultiValueMap*/
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user@user.us");
        loginRequest.setPassword("user");

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<?> httpEntity = new HttpEntity<>(loginRequest, requestHeaders);

        ResponseEntity<LoginResponse> loginResponse = restTemplate
                // .exchange("http://localhost:7070/authenticate"
                .exchange("http://localhost:9100/auth"
                        , HttpMethod.POST, httpEntity, LoginResponse.class);
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


