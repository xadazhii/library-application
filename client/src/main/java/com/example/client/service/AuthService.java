package com.example.client.service;

import com.example.client.token.TokenStorage;
import static com.example.client.service.ValidationService.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class AuthService {

    private static final String BASE_URL = "http://localhost:8080/auth/";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static String register(String username, String email, String password) {
        String validationResult = ValidationService.validateRegistration(username, email, password);

        if (!validationResult.equals("success")) {
            return validationResult;
        }

        String json = String.format("{\"username\": \"%s\", \"email\": \"%s\", \"password\": \"%s\"}", username, email, password);
        return sendRequest("register", json);
    }

    public static String login(String username, String password) {
        String validationResult = ValidationService.validateLogin(username, password);

        if (!validationResult.equals("success")) {
            return validationResult;
        }

        String json = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);
        String response = sendRequest("login", json);
        if (response.startsWith("Login successful")) {
            saveToken(response);
        }
        return response;
    }

    private static String sendRequest(String endpoint, String json) {
        try {
            HttpRequest request = buildRequest(endpoint, json);
            HttpResponse<String> response = sendHttpRequest(request);

            if (response.statusCode() == 200) {
                return handleSuccessResponse(endpoint, response.body());
            } else {
                return handleError(response);
            }

        } catch (Exception e) {
            return "Request failed: " + e.getMessage();
        }
    }

    private static HttpRequest buildRequest(String endpoint, String json) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
    }

    private static HttpResponse<String> sendHttpRequest(HttpRequest request) throws Exception {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void saveToken(String response) {
        String token = response.replace("Login successful. Token: ", "").substring(16).replace("\"", "").replace("}", "");
        TokenStorage.setAuthToken(token);
        isTokenSaved();
    }
}