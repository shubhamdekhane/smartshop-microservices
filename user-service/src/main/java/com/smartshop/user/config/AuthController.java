package com.smartshop.user.config;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartshop.user.dto.AuthRequest;
import com.smartshop.user.dto.AuthResponse;
import com.smartshop.user.entity.User;
import com.smartshop.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager 
        authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        User user = userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> 
                new RuntimeException("User not found!"));

        String token = jwtService.generateToken(
            user.getEmail(), user.getRole());

        return ResponseEntity.ok(
            AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build()
        );
    }
}