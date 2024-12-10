package com.example.client.service;

import com.example.client.dto.BookDTO;
import com.example.client.util.HttpUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import java.util.Collections;
import java.util.List;

public class MyBooksService {

    private static final String BASE_URL = "http://localhost:8080/library";

    public List<BookDTO> getBorrowedBooks() {

        String url = BASE_URL + "/myBooksGet";

        try {
            return HttpUtils.executeRequest(HttpMethod.GET, url, null, new ParameterizedTypeReference<>() {
            });
        } catch (RuntimeException e) {
            System.err.println("Failed to fetch borrowed books: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}