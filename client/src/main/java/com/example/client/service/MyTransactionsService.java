package com.example.client.service;

import com.example.client.dto.TransactionsDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.example.client.util.HttpUtils.createHeaders;
import static com.example.client.util.HttpUtils.restTemplate;

public class MyTransactionsService {

    private static final String BASE_URL = "http://localhost:8080/transactions";

    public List<TransactionsDTO> fetchUserTransactions(Integer userId) {
        String url = BASE_URL + "/myTransactionsGet/" + userId;
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders()), String.class);
            String responseBody = response.getBody();
            return convertJsonToTransactions(responseBody);
        } catch (RuntimeException e) {
            System.err.println("Error occurred while fetching transactions: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<TransactionsDTO> convertJsonToTransactions(String json) {
        try {
            ObjectMapper objectMapper = createObjectMapper();
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            System.err.println("Failed to convert JSON: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        return objectMapper;
    }

    public List<TransactionsDTO> searchTransactions(List<TransactionsDTO> transactions, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return new ArrayList<>(transactions);
        }

        List<TransactionsDTO> filtered = new ArrayList<>();
        for (TransactionsDTO transaction : transactions) {
            if (String.valueOf(transaction.getTransactionId()).contains(searchText) ||
                    String.valueOf(transaction.getUserId().getId()).contains(searchText) ||
                    String.valueOf(transaction.getBookId().getId()).contains(searchText) ||
                    transaction.getAction().toLowerCase().contains(searchText.toLowerCase()) ||
                    transaction.getDate().toString().contains(searchText)) {
                filtered.add(transaction);
            }
        }
        return filtered;
    }
}
