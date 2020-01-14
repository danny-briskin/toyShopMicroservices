package ua.com.univerpulse.toyshop.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@Log4j2
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // We use auth manager to validate the user credentials
    private AuthenticationManager authManager;

    private final JwtConfig jwtConfig;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager
            , @NotNull JwtConfig jwtConfig) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;

        // By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
        // In our case, we use "/auth". So, we need to override the defaults.
        this.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(jwtConfig.getUri()));
    }

    @Override
    public Authentication attemptAuthentication(@NotNull HttpServletRequest request
            , HttpServletResponse response)
            throws AuthenticationException {

//        try {
//            StringBuilder textBuilder = new StringBuilder();
//            try (Reader reader = new BufferedReader(new InputStreamReader
//                    (request.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
//                int c = 0;
//                while ((c = reader.read()) != -1) {
//                    textBuilder.append((char) c);
//                }
//            }
//            log.warn(textBuilder.toString());

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // 1. Get credentials from request
//            UserCredentials creds = new ObjectMapper()
//                    .readValue(request.getInputStream(), UserCredentials.class);

        // 2. Create auth object (contains credentials) which will be used by auth manager
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, password, Collections.emptyList());

        // 3. Authentication manager authenticate the user, and use UserDetialsServiceImpl::loadUserByUsername() method to load the user.
        return authManager.authenticate(authToken);

//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    // Upon successful authentication, generate a token.
    // The 'auth' passed to successfulAuthentication() is the current authenticated user.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain chain,
                                            @NotNull Authentication auth) throws IOException, ServletException {

        long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(auth.getName())
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();

        // Add token to header
        log.warn("token granted [" + token + "]");
        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
    }

    // A (temporary) class just to represent the user credentials
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class UserCredentials {
        private String username;
        private String password;
    }
}