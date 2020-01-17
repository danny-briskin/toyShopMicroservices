package ua.com.univerpulse.toyshop.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@Log4j2
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request
            , HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            log.warn("Have a request with [" + jwtToken + "]");
            try {
                if (!jwtTokenUtil.isTokenExpired(jwtToken)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = jwtTokenUtil.getUserWithAuthorities(jwtToken);
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.warn(usernamePasswordAuthenticationToken.getAuthorities());
                    SecurityContextHolder.getContext()
                            .setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    log.error("JWT Token expired [" + jwtToken + "]");
                }
            } catch (IllegalArgumentException e) {
                log.warn("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.warn("JWT Token has expired");
            }
        } else {
            log.warn("JWT Token does not begin with Bearer String [" + requestTokenHeader + ']');
        }
        chain.doFilter(request, response);
    }
}