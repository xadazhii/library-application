package com.example.server.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import java.util.Optional;

public class JwtUtils {

    // Authorization: Bearer <token>
    static Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if(StringUtils.hasText(token) && token.startsWith("Bearer "))
            return Optional.of(token.substring(7));
        return Optional.empty();
    }
}
