package com.example.server.controllers;

import com.example.server.dto.AuthResponse;
import com.example.server.dto.LoginRequest;
import com.example.server.dto.RegisterRequest;
import com.example.server.entity.UserEntity;
import com.example.server.repositories.UserRepo;
import com.example.server.security.JwtIssuer;
import com.example.server.services.AuthService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtIssuer jwtIssuer;
    private final UserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Validated LoginRequest loginRequest) {
        if (!userRepo.existsByUsername(loginRequest.getUsername())) {
            return ResponseEntity.notFound().build();
        }
        authService.authenticate(loginRequest);
        UserEntity userEntity = userRepo.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        var token = jwtIssuer.generateToken(
                userEntity.getUsername(),
                userEntity.getId(),
                "ROLE_USER"
        );
        return ResponseEntity.ok(AuthResponse.builder().accessToken(token).build());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }
}