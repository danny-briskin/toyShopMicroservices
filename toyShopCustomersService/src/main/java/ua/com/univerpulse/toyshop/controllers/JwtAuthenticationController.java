package ua.com.univerpulse.toyshop.controllers;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ua.com.univerpulse.toyshop.config.security.JwtTokenUtil;
import ua.com.univerpulse.toyshop.model.jwt.JwtRequest;
import ua.com.univerpulse.toyshop.model.jwt.JwtResponse;
import ua.com.univerpulse.toyshop.model.services.AppUserDetailService;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@RestController
@CrossOrigin
@Log4j2
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AppUserDetailService appUserDetailService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<JwtResponse> createAuthenticationToken
            (@NotNull @RequestBody JwtRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = appUserDetailService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            log.catching(e);
        }
    }


}