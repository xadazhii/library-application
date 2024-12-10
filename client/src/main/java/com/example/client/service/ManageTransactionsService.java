package com.example.client.service;

import com.example.client.dto.TransactionsDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.client.util.HttpUtils.executeRequestWithResponse;

public class ManageTransactionsService {

    private static final String BASE_URL = "http://localhost:8080/transactions";

    public List<TransactionsDTO> getAllTransactions() {
        String url = BASE_URL + "/getAll";
        try {
            return executeRequestWithResponse(HttpMethod.GET, url, new ParameterizedTypeReference<>() {});
        } catch (RuntimeException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean deleteTransaction(Integer transactionId) {
        String url = BASE_URL + "/delete/" + transactionId;
        try {
            executeRequestWithResponse(HttpMethod.DELETE, url, new ParameterizedTypeReference<Void>() {});
            return true;
        } catch (RuntimeException e) {
            System.err.println("Failed to delete transaction: " + e.getMessage());
            return false;
        }
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