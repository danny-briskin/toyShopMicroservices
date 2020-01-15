package ua.com.univerpulse.toyshop.security;

import lombok.extern.log4j.Log4j2;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@Log4j2
public class JwtUsernameAndPasswordAuthenticationFilter
//        extends UsernamePasswordAuthenticationFilter
{
//
//    // We use auth manager to validate the user credentials
//    private AuthenticationManager authManager;
//
//    private final JwtConfig jwtConfig;
//
//    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager
//            , @NotNull JwtConfig jwtConfig) {
//        this.authManager = authManager;
//        this.jwtConfig = jwtConfig;
//
//        // By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
//        // In our case, we use "/auth". So, we need to override the defaults.
//        this.setRequiresAuthenticationRequestMatcher(
//                new AntPathRequestMatcher(jwtConfig.getUri()));
//    }
//
//    @Override
//    public Authentication attemptAuthentication(@NotNull HttpServletRequest request
//            , HttpServletResponse response) {
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                username, password, Collections.emptyList());
//        return authManager.authenticate(authToken);
//    }
//
//    // Upon successful authentication, generate a token.
//    // The 'auth' passed to successfulAuthentication() is the current authenticated user.
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain chain,
//                                            @NotNull Authentication auth) {
//        long now = System.currentTimeMillis();
//        String token = Jwts.builder()
//                .setSubject(auth.getName())
//                // Convert to list of strings.
//                // This is important because it affects the way we get them back in the Gateway.
//                .claim("authorities", auth.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
//                .setIssuedAt(new Date(now))
//                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
//                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
//                .compact();
//        // Add token to header
//        log.warn("token granted [" + token + "]");
//        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
//    }
//
//    // A (temporary) class just to represent the user credentials
//    @Data
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    private static class UserCredentials {
//        private String username;
//        private String password;
//    }
}