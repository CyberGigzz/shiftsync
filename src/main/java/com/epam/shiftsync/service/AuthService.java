package com.epam.shiftsync.service;

import com.epam.shiftsync.dto.auth.AuthResponse;
import com.epam.shiftsync.dto.auth.LoginRequest;
import com.epam.shiftsync.dto.auth.RegisterRequest;
import com.epam.shiftsync.entity.User;
import com.epam.shiftsync.repository.UserRepository;
import com.epam.shiftsync.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        var user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password())); 
        user.setRole("EMPLOYEE"); 

        userRepository.save(user);

        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(user.getRole())
                .build();
        
        var jwtToken = jwtService.generateToken(userDetails);
        
        return new AuthResponse(jwtToken, user.getId(), user.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(); 

        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash()) 
                .authorities(user.getRole())
                .build();
        
        var jwtToken = jwtService.generateToken(userDetails);
        
        return new AuthResponse(jwtToken, user.getId(), user.getRole());
    }
}