package com.quest.etna.config;
import com.quest.etna.model.*;
import com.quest.etna.repositories.UserRepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final JwtTokenUtil jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationService(UserRepository repository, JwtTokenUtil jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e){
            throw new Exception(e.getMessage());
        }

         
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user.getUsername());
        return new AuthenticationResponse(jwtToken);
    }

    public MeResponse register(User user) {
        repository.save(user);
        var jwtToken = jwtService.generateToken(user.getUsername());
        return new MeResponse(user.getUsername(), user.getRole());
    }
}
