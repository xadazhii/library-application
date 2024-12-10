package com.example.client.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lombok.RequiredArgsConstructor;
import java.util.Objects;
import java.util.Random;

@RequiredArgsConstructor
public class BookCardManager {

    @FXML
    private final GridPane booksContainer;

    public void addBookCard(String title, String author, String isbn) {
        String selectedImagePath = getString();

        VBox bookCard = new VBox(10);
        bookCard.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e1e1e1; -fx-border-radius: 15; -fx-padding: 15; -fx-effect: dropshadow(gaussian, #34495e, 10, 0, 0, 5); -fx-min-width: 200px; -fx-max-width: 200px;");
        bookCard.setAlignment(Pos.CENTER);

        HBox titleContainer = new HBox();
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setStyle("-fx-padding: 5;");

        Label bookTitle = new Label(title);
        bookTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        titleContainer.getChildren().add(bookTitle);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(120);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-radius: 10; -fx-effect: dropshadow(gaussian, #7f8c8d, 10, 0, 0, 5);");

        Image image = new Image(Objects.requireNonNull(getClass().getResource(selectedImagePath)).toExternalForm());
        imageView.setImage(image);

        VBox authorBox = new VBox();
        authorBox.setAlignment(Pos.CENTER_LEFT);
        Label authorLabel = new Label("Author:");
        authorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label authorValue = new Label(author);
        authorValue.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50;");
        authorBox.getChildren().addAll(authorLabel, authorValue);

        VBox isbnBox = new VBox();
        isbnBox.setAlignment(Pos.CENTER_RIGHT);
        Label isbnLabel = new Label("ISBN:");
        isbnLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label isbnValue = new Label(isbn);
        isbnValue.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50;");
        isbnBox.getChildren().addAll(isbnLabel, isbnValue);

        HBox authorIsbnContainer = new HBox();
        authorIsbnContainer.setStyle("-fx-padding: 5; -fx-min-width: 100%;");
        authorIsbnContainer.setAlignment(Pos.CENTER);
        HBox.setHgrow(authorBox, Priority.ALWAYS);
        HBox.setHgrow(isbnBox, Priority.ALWAYS);
        authorIsbnContainer.getChildren().addAll(authorBox, isbnBox);

        bookCard.getChildren().addAll(titleContainer, imageView, authorIsbnContainer);

        int row = booksContainer.getChildren().size() / 3;
        int col = booksContainer.getChildren().size() % 3;

        booksContainer.add(bookCard, col, row);
    }

    private static String getString() {
        String[] coverImages = {
                "/com/example/client/images/book1.png",
                "/com/example/client/images/book2.png",
                "/com/example/client/images/book3.png",
                "/com/example/client/images/book4.png",
                "/com/example/client/images/book5.png",
                "/com/example/client/images/book6.png",
                "/com/example/client/images/book7.png",
                "/com/example/client/images/book8.png",
                "/com/example/client/images/book9.png",
        };
        Random random = new Random();
        return coverImages[random.nextInt(coverImages.length)];
    }
}
