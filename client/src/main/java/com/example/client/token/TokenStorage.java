package com.example.client.token;

import lombok.Getter;
import lombok.Setter;

public class TokenStorage {
    @Getter
    @Setter
    private static String authToken = null;

    public static boolean isAuthTokenSaved() {
        return authToken != null && !authToken.isEmpty();
    }
}
