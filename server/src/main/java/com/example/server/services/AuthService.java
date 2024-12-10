package com.example.server.services;

import com.example.server.dto.LoginRequest;
import com.example.server.dto.RegisterRequest;
import com.example.server.entity.RoleEntity;
import com.example.server.entity.UserEntity;
import com.example.server.repositories.RoleRepo;
import com.example.server.repositories.UserRepo;
import com.example.server.security.JwtIssuer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final JwtIssuer jwtIssuer;
    private final RoleRepo roleRepo;

    public void register(RegisterRequest registerRequest) {
        if (userRepo.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        RoleEntity defaultRole = (RoleEntity) roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("Default role USER not found"));

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerRequest.getUsername());
        userEntity.setEmail(registerRequest.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userEntity.setRoleId(Math.toIntExact(defaultRole.getId()));

        userRepo.save(userEntity);
    }

    public void authenticate(LoginRequest loginRequest) {
        UserEntity userEntity = userRepo.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String roleName = roleRepo.findById(userEntity.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found for user"))
                .getName();

        jwtIssuer.generateToken(loginRequest.getUsername(), userEntity.getId(), roleName);
    }

    @PostConstruct
    public void initializeAdmin() {
        if (!userRepo.existsByUsername("admin")) {
            RoleEntity adminRole = (RoleEntity) roleRepo.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new IllegalArgumentException("Default role ADMIN not found"));

            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoleId(Math.toIntExact(adminRole.getId()));

            userRepo.save(admin);
        }
    }
}
