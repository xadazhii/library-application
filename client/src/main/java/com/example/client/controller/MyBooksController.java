package com.example.client.controller;

import com.example.client.MainClient;
import com.example.client.dto.BookDTO;
import com.example.client.service.MyBooksService;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.util.List;

public class MyBooksController {

    @FXML ImageView backgroundImage;
    @FXML private GridPane booksContainer;

    private final MyBooksService myBooksService = new MyBooksService();

    @FXML
    public void initialize() {
        BookCardManager bookCardManager = new BookCardManager(booksContainer);
        List<BookDTO> borrowedBooks = myBooksService.getBorrowedBooks();

        for (BookDTO book : borrowedBooks) {
            bookCardManager.addBookCard(book.getTitle(), book.getAuthor(), book.getIsbn());
        }
    }

    @FXML
    private void handleBackToAccount() throws IOException {
        MainClient.loadUserAccountScene();
    }
}