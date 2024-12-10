package com.example.client.service;

import com.example.client.token.TokenStorage;
import java.net.http.HttpResponse;

public class ValidationService {

    public boolean areFieldsEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean isPasswordValid(String password) {
        return password == null || password.length() < 4;
    }

    public boolean isEmailValid(String email) {
        return email != null && email.contains("@");
    }

    public static String validateLogin(String username, String password) {
        ValidationService validationService = new ValidationService();

        if (validationService.areFieldsEmpty(username, password)) {
            return "Please fill in all fields.";
        }

        if (validationService.isPasswordValid(password)) {
            return "Password must be at least 4 characters.";
        }
        return "success";
    }

    public static String validateRegistration(String username, String email, String password) {
        ValidationService validationService = new ValidationService();

        if (validationService.areFieldsEmpty(username, email, password)) {
            return "Please fill in all fields.";
        }

        if (email != null && !validationService.isEmailValid(email)) {
            return "Email must contain '@' symbol.";
        }

        if (validationService.isPasswordValid(password)) {
            return "Password must be at least 4 characters.";
        }
        return "success";
    }

    public static String handleSuccessResponse(String endpoint, String responseBody) {
        if ("login".equals(endpoint)) {
            return "Login successful. Token: " + responseBody;
        } else if ("register".equals(endpoint)) {
            return "User registered successfully.";
        } else {
            return "Request successful.";
        }
    }

    public static String handleError(HttpResponse<String> response) {
        return handleErrorResponse(response);
    }

    private static String handleErrorResponse(HttpResponse<String> response) {
        return switch (response.statusCode()) {
            case 400 -> "Bad Request: " + response.body();
            case 401 -> "Incorrect username or password.";
            case 404 -> "User not found";
            case 500 -> "Internal Server Error";
            default -> "Unexpected error: " + response.statusCode() + " - " + response.body();
        };
    }

    public static void isTokenSaved() {
        if (TokenStorage.isAuthTokenSaved()) {
            AlertService.showInfo("Token Saved", "Login successful!");
        } else {
            AlertService.showError("Token Error", "Login successful, but failed to save token.");
        }
    }
}
