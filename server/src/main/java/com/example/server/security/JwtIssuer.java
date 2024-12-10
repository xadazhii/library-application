package com.example.server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.temporal.ChronoUnit;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtIssuer {

    private final JwtProperties jwtProperties;

    public String generateToken(String username,Integer userId,String role) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Date.from(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS))))
                .withClaim("name", username)
                .withClaim("role", role)
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey()));
    }
}
