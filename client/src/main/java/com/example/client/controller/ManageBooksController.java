package com.example.client.controller;

import com.example.client.MainClient;
import com.example.client.dto.BookDTO;
import com.example.client.service.AlertService;
import com.example.client.service.ManageBooksService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class ManageBooksController {

    @FXML public Button addBookButton, deleteBookButton;
    @FXML ImageView backgroundImage;
    @FXML private TextField bookTitleField, bookAuthorField, bookIsbnField, bookCopiesField;
    @FXML private TableView<BookDTO> booksTable;
    @FXML private TableColumn<BookDTO, String> bookTitleColumn, bookAuthorColumn, bookIsbnColumn;
    @FXML private TableColumn<BookDTO, Integer> bookIdColumn, bookCopiesColumn;

    private final ObservableList<BookDTO> booksList = FXCollections.observableArrayList();
    private final ManageBooksService manageBooksService = new ManageBooksService();

    @FXML
    public void initialize() {
        initializeTableColumns();
        loadBooksFromDatabase();
        booksTable.setOnMouseClicked(this::onTableRowClick);
    }

    private void initializeTableColumns() {
        bookIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        bookTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        bookAuthorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        bookIsbnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsbn()));
        bookCopiesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAvailableCopies()).asObject());
    }

    private void loadBooksFromDatabase() {
        try {
            booksList.clear();
            booksList.addAll(manageBooksService.getAllBooks());
            booksTable.setItems(booksList);
        } catch (Exception e) {
            AlertService.showError("Error", "Failed to load books.");
        }
    }

    @FXML
    public void onAddBookButtonClick() {
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();
        String isbn = bookIsbnField.getText();
        String copies = bookCopiesField.getText();

        if (areFieldsValid(title, author, isbn, copies)) {
            try {
                int numCopies = Integer.parseInt(copies);
                BookDTO newBook = new BookDTO(title, author, isbn, numCopies);

                if (manageBooksService.addBook(newBook)) {
                    AlertService.showInfo("Success", "Book added successfully.");
                    loadBooksFromDatabase();
                } else {
                    AlertService.showError("Error", "Failed to add book.");
                }
            } catch (NumberFormatException e) {
                AlertService.showError("Error", "Invalid number of copies.");
            }
        }
    }

    @FXML
    public void onDeleteBookButtonClick() {
        BookDTO selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            showConfirmationAlert("Are you sure you want to delete the book: " + selectedBook.getTitle() + "?")
                    .ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            deleteBook(selectedBook);
                        }
                    });
        } else {
            AlertService.showError("Error", "No book selected.");
        }
    }

    private void deleteBook(BookDTO selectedBook) {
        try {
            if (manageBooksService.deleteBookById(selectedBook.getId())) {
                booksList.remove(selectedBook);
                AlertService.showInfo("Success", "Book deleted successfully.");
            } else {
                AlertService.showError("Error", "Failed to delete book.");
            }
        } catch (Exception e) {
            AlertService.showError("Error", "An error occurred while deleting the book.");
        }
    }

    private void openEditBookDialog(BookDTO selectedBook) {
        TextField titleField = new TextField(selectedBook.getTitle());
        TextField authorField = new TextField(selectedBook.getAuthor());
        TextField isbnField = new TextField(selectedBook.getIsbn());
        TextField copiesField = new TextField(String.valueOf(selectedBook.getAvailableCopies()));

        Alert alert = createEditBookAlert(selectedBook, titleField, authorField, isbnField, copiesField);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                updateBookInfo(selectedBook, titleField, authorField, isbnField, copiesField);
            }
        });
    }

    private Alert createEditBookAlert(BookDTO selectedBook, TextField titleField, TextField authorField, TextField isbnField, TextField copiesField) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edit Book Information");
        alert.setHeaderText("Edit the book information for " + selectedBook.getTitle());

        Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/example/client/images/book_alert.png")).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        alert.setGraphic(imageView);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("ISBN:"), 0, 2);
        grid.add(isbnField, 1, 2);
        grid.add(new Label("Copies:"), 0, 3);
        grid.add(copiesField, 1, 3);

        alert.getDialogPane().setContent(grid);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        return alert;
    }

    private void updateBookInfo(BookDTO selectedBook, TextField titleField, TextField authorField, TextField isbnField, TextField copiesField) {
        if (areFieldsValid(titleField.getText(), authorField.getText(), isbnField.getText(), copiesField.getText())) {
            try {
                int numCopies = Integer.parseInt(copiesField.getText());

                selectedBook.setTitle(titleField.getText());
                selectedBook.setAuthor(authorField.getText());
                selectedBook.setIsbn(isbnField.getText());
                selectedBook.setAvailableCopies(numCopies);

                if (manageBooksService.updateBook(selectedBook)) {
                    AlertService.showInfo("Success", "Book information updated successfully.");
                    loadBooksFromDatabase();
                } else {
                    AlertService.showError("Error", "Failed to update book.");
                }
            } catch (NumberFormatException e) {
                AlertService.showError("Error", "Invalid number of copies.");
            }
        }
    }

    private Optional<ButtonType> showConfirmationAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(content);
        return alert.showAndWait();
    }

    @FXML
    private void onTableRowClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            BookDTO selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                openEditBookDialog(selectedBook);
            }
        }
    }

    @FXML
    public void onBackButtonClick() throws IOException {
        MainClient.loadAdminPanelScene();
    }

    private boolean areFieldsValid(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                AlertService.showError("Error", "All fields must be filled.");
                return false;
            }
        }
        return true;
    }
}
