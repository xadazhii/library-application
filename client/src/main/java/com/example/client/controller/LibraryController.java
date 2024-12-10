package com.example.client.controller;

import com.example.client.MainClient;
import com.example.client.dto.BookDTO;
import com.example.client.service.LibraryService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import static com.example.client.service.AlertService.showAlert;

public class LibraryController {

    @FXML public Label statusLabel;
    @FXML ImageView backgroundImage;
    @FXML private TableView<BookDTO> booksTableView;
    @FXML private TableColumn<BookDTO, Integer> bookIdColumn;
    @FXML private TableColumn<BookDTO, String> bookTitleColumn;
    @FXML private TableColumn<BookDTO, String> bookAuthorColumn;
    @FXML private TableColumn<BookDTO, String> bookIsbnColumn;
    @FXML private TableColumn<BookDTO, Integer> bookCopiesColumn;
    @FXML private TextField searchTextField;

    private final LibraryService libraryService = new LibraryService();

    @FXML
    public void initialize() {
        initializeTableColumns();
        loadBooksFromServer();
        setupSearchField();
        setupTableViewDoubleClick();
    }

    private void initializeTableColumns() {
        bookIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        bookTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        bookAuthorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        bookIsbnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsbn()));
        bookCopiesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAvailableCopies()).asObject());
    }

    private void setupSearchField() {
        searchTextField.setOnKeyReleased(this::filterBooksBySearch);
    }

    private void setupTableViewDoubleClick() {
        booksTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                BookDTO selectedBook = booksTableView.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    showBookDetails(selectedBook);
                }
            }
        });
    }

    private void loadBooksFromServer() {
        try {
            ObservableList<BookDTO> booksData = libraryService.loadBooksData();
            booksTableView.setItems(booksData);
        } catch (Exception e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
    }

    private void filterBooksBySearch(KeyEvent event) {
        String searchText = searchTextField.getText();
        ObservableList<BookDTO> filteredBooks = libraryService.filterBooksBySearch(searchText);
        booksTableView.setItems(filteredBooks);
    }

    @FXML
    private void onSearchButtonClick() {
        String searchText = searchTextField.getText();
        ObservableList<BookDTO> filteredBooks = libraryService.filterBooksBySearch(searchText);
        booksTableView.setItems(filteredBooks);
    }

    @FXML
    private void onBackButtonClick() throws IOException {
        MainClient.loadUserAccountScene();
    }

    private void showBookDetails(BookDTO book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Book Details");
        alert.setHeaderText("Details for: " + book.getTitle());

        Image image = new Image(getClass().getResource("/com/example/client/images/library_alert.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        alert.setGraphic(imageView);

        String details = "Author: " + book.getAuthor() + "\n" + "ISBN: " + book.getIsbn() + "\n" + "Copies available: " + book.getAvailableCopies();
        alert.setContentText(details);

        ButtonType actionButton;
        if (book.getAvailableCopies() == 0) {
            actionButton = new ButtonType("Not Available", ButtonBar.ButtonData.CANCEL_CLOSE);
        } else if (libraryService.isBookBorrowedByUser(book)) {
            actionButton = new ButtonType("Return");
        } else {
            actionButton = new ButtonType("Borrow");
        }
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(actionButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == actionButton && actionButton.getText().equals("Borrow")) {
                borrowBook(book);
            } else if (response == actionButton && actionButton.getText().equals("Return")) {
                returnBook(book);
            }
        });
    }

    private void borrowBook(BookDTO book) {
        boolean success = libraryService.borrowBook(book);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book borrowed successfully.");
            loadBooksFromServer();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "You have already borrowed this book.");
        }
    }

    private void returnBook(BookDTO book) {
        boolean success = libraryService.returnBook(book);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book returned successfully.");
            loadBooksFromServer();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "You have not borrowed this book.");
        }
    }
}
