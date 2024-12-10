package com.example.client.service;

import com.example.client.dto.BookDTO;
import com.example.client.util.HttpUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import java.util.Collections;
import java.util.List;

public class ManageBooksService {

    private static final String BASE_URL = "http://localhost:8080/library";

    public boolean addBook(BookDTO book) {
        if (isIsbnDuplicate(book.getIsbn())) {
            System.err.println("A book with this ISBN already exists.");
            return false;
        }

        String url = BASE_URL + "/booksAdd";

        try {
            HttpUtils.executeRequest(HttpMethod.POST, url, book, new ParameterizedTypeReference<Void>() {});
            return true;
        } catch (RuntimeException e) {
            System.err.println("Failed to add book: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBookById(Integer id) {
        String url = BASE_URL + "/booksDelete/" + id;
        try {
            HttpUtils.executeRequest(HttpMethod.DELETE, url, null, new ParameterizedTypeReference<Void>() {});
            return true;
        } catch (RuntimeException e) {
            System.err.println("Failed to delete book: " + e.getMessage());
            return false;
        }
    }

    public boolean updateBook(BookDTO book) {
        String url = BASE_URL + "/booksUpdate/" + book.getId();
        try {
            HttpUtils.executeRequest(HttpMethod.PUT, url, book, new ParameterizedTypeReference<Void>() {});
            return true;
        } catch (RuntimeException e) {
            System.err.println("Failed to update book: " + e.getMessage());
            return false;
        }
    }

    public List<BookDTO> getAllBooks() {
        String url = BASE_URL + "/booksGet";
        try {
            return HttpUtils.executeRequest(HttpMethod.GET, url, null, new ParameterizedTypeReference<>() {
            });
        } catch (RuntimeException e) {
            System.err.println("Failed to fetch books: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private boolean isIsbnDuplicate(String isbn) {
        List<BookDTO> allBooks = getAllBooks();
        return allBooks.stream().anyMatch(book -> book.getIsbn().equals(isbn));
    }
}