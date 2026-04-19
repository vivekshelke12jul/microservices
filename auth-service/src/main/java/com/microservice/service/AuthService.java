package com.microservice.service;

import com.microservice.exchange.request.LoginRequest;
import com.microservice.exchange.request.RegisterRequest;
import com.microservice.exchange.response.AuthResponse;
import com.microservice.model.AppUser;
import com.microservice.model.Role;
import com.microservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtGeneratorService jwtGeneratorService;

    @Autowired
    AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {


        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String jwtToken = jwtGeneratorService.generateToken(user.getUsername(), List.of(new SimpleGrantedAuthority("USER")));
        userRepository.save(user);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .build();

    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String jwtToken = jwtGeneratorService.generateToken(request.getUsername(), List.of(new SimpleGrantedAuthority("USER")));
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}
