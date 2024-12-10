package com.example.client.util;

import com.example.client.token.TokenStorage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class HttpUtils {

    public static final RestTemplate restTemplate = new RestTemplate();

    public static HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAuthToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public static String getAuthToken() {
        String authToken = TokenStorage.getAuthToken();

        if (authToken == null || authToken.isEmpty()) {
            throw new RuntimeException("No authentication token found");
        }
        return authToken;
    }

    public static <T> T executeRequest(HttpMethod method, String url, Object body, ParameterizedTypeReference<T> responseType, Object... params) {
        HttpEntity<?> entity = new HttpEntity<>(body, createHeaders());
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, method, entity, responseType, params);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            System.err.println("HTTP Error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Request failed with status: " + e.getStatusCode(), e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

    public static <T> T executeRequestWithResponse(HttpMethod method, String url, ParameterizedTypeReference<T> responseType) {
        HttpEntity<?> entity = new HttpEntity<>(null, createHeaders());
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, method, entity, responseType);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            System.err.println("HTTP Error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Request failed with status: " + e.getStatusCode(), e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }
}