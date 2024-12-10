package com.example.server.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse{
    private final String accessToken;
}