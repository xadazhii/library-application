package com.example.server.controllers;

import com.example.server.entity.UserEntity;
import com.example.server.repositories.UserRepo;
import com.example.server.security.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/me")
public class InformationController {

    private final JwtDecoder jwtDecoder;
    private final UserRepo userRepo;

    @GetMapping("/info")
    public ResponseEntity<UserEntity> me(@RequestHeader("Authorization") String jwt) {
        String token = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        UserEntity user  = userRepo.findById(Integer.parseInt(jwtDecoder.decode(token).getSubject())).get();
        return ResponseEntity.ok(user);
    }
}
