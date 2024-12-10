package com.example.client.service;

import com.example.client.dto.BookDTO;
import com.example.client.util.HttpUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import java.util.List;

public class LibraryService {

    private static final String BASE_URL = "http://localhost:8080/library";
    private final ObservableList<BookDTO> booksObservableList = FXCollections.observableArrayList();

    public ObservableList<BookDTO> loadBooksData() {
        try {
            String url = BASE_URL + "/booksGet";
            List<BookDTO> booksList = HttpUtils.executeRequest(HttpMethod.GET, url, null, new ParameterizedTypeReference<>() {
            });

            booksObservableList.clear();

            if (booksList != null && !booksList.isEmpty()) {
                booksObservableList.addAll(booksList);
            }
        } catch (RuntimeException e) {
            System.err.println("Error fetching books: " + e.getMessage());
        }
        return booksObservableList;
    }

    public ObservableList<BookDTO> filterBooksBySearch(String searchText) {
        searchText = searchText.toLowerCase();
        if (searchText.isEmpty()) {
            return booksObservableList;
        } else {
            String finalSearchText = searchText;
            return booksObservableList.filtered(book ->
                    book.getTitle().toLowerCase().contains(finalSearchText) ||
                            book.getAuthor().toLowerCase().contains(finalSearchText) ||
                            book.getIsbn().toLowerCase().contains(finalSearchText)
            );
        }
    }

    public boolean borrowBook(BookDTO bookId) {
        String url = BASE_URL + "/myBookBorrow/" + bookId.getId();
        try {
            HttpUtils.executeRequest(HttpMethod.POST, url, null, new ParameterizedTypeReference<Void>() {});
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean returnBook(BookDTO bookId) {
        String url = BASE_URL + "/myBookReturn/" + bookId.getId();
        try {
            HttpUtils.executeRequest(HttpMethod.DELETE, url, null, new ParameterizedTypeReference<Void>() {});
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean isBookBorrowedByUser(BookDTO book) {
        List<BookDTO> borrowedBooks = new MyBooksService().getBorrowedBooks();
        return borrowedBooks.stream().anyMatch(b -> b.getId().equals(book.getId()));
    }
}