package com.example.client.controller;

import com.example.client.MainClient;
import com.example.client.dto.UserDTO;
import com.example.client.service.AlertService;
import com.example.client.service.UserInfoService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import javafx.util.Pair;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import static com.example.client.service.AlertService.showError;
import static com.example.client.service.AlertService.showInfo;

@Data
@RequiredArgsConstructor
public class AccountController {

    @FXML ImageView backgroundImage, userImage;
    @FXML Hyperlink settingsLink;
    @FXML private Label usernameLabel, emailLabel;
    @FXML private Button helpButton;
    @FXML private Hyperlink myBooksLink, libraryLink, adminLink;
    @FXML private HBox dynamicButtonBox, settingsButtonBox, adminButtonBox;
    @FXML private Pane videoPane;

    private final UserInfoService userInfoService = new UserInfoService();

    @FXML
    public void initialize() {
        UserDTO user = userInfoService.fetchUserInfo();
        usernameLabel.setText("username: " + user.getUsername());
        emailLabel.setText("email: " + user.getEmail());

        if (user.getRoleId() == 1) {
            adminButtonBox.setVisible(true);
            settingsButtonBox.setVisible(false);
        } else {
            settingsButtonBox.setVisible(true);
            adminButtonBox.setVisible(false);
        }
        loadUsersFromDatabase();
    }

    @FXML
    private void onUpdateUser() {
        try {
            UserDTO currentUser = userInfoService.fetchUserInfo();
            UserDTO currentUser2 = userInfoService.fetchUserInfo();

            if (currentUser == null) {
                showError("Error", "Failed to load user data.");
                return;
            }

            TextField usernameField = new TextField(currentUser.getUsername());
            TextField emailField = new TextField(currentUser.getEmail());

            Alert editUserAlert = createEditUserAlert(currentUser, usernameField, emailField);
            Optional<ButtonType> result = editUserAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                handleDialogResult(
                        new Pair<>(usernameField.getText(), emailField.getText()),
                        currentUser,
                        currentUser2
                );
            }

        } catch (Exception e) {
            showError("Error", "An error occurred: " + e.getMessage());
        }
    }

    private Alert createEditUserAlert(UserDTO currentUser, TextField usernameField, TextField emailField) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edit User Information");
        alert.setHeaderText("Edit the user information for " + currentUser.getUsername());

        Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/example/client/images/user.png")).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        alert.setGraphic(imageView);

        usernameField.setPromptText("Username");
        emailField.setPromptText("Email");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);

        alert.getDialogPane().setContent(grid);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        return alert;
    }

    private void handleDialogResult(Pair<String, String> pair, UserDTO currentUser, UserDTO currentUser2) {
        String newUsername = pair.getKey().trim();
        String newEmail = pair.getValue().trim();

        if (newUsername.isBlank() || newEmail.isBlank()) return;

        if (newUsername.equals(currentUser.getUsername()) && newEmail.equals(currentUser.getEmail())) {
            showInfo("Info", "No changes detected.");
            return;
        }

        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);

        boolean isUpdated = userInfoService.updateUser(currentUser, currentUser2.getUsername());

        if (isUpdated) {
            showInfo("Success", "Your profile has been updated.");
            loadUsersFromDatabase();
        } else {
            showError("Error", "Failed to update your profile.");
        }
    }

    private void loadUsersFromDatabase() {
        try {
            UserDTO currentUser = userInfoService.fetchUserInfo();

            if (currentUser == null) {
                AlertService.showError("Error", "Failed to load user data.");
                return;
            }

            usernameLabel.setText("username: " + currentUser.getUsername());
            emailLabel.setText("email: " + currentUser.getEmail());
        } catch (Exception e) {
            AlertService.showError("Error", "An error occurred while loading user data: " + e.getMessage());
        }
    }

    @FXML
    private void onPersonalDataButtonClick() throws IOException {
        Scene currentScene = usernameLabel.getScene();

        if (currentScene.getRoot().getId() != null && currentScene.getRoot().getId().equals("myTransactionsRoot")) {
            return;
        }
        MainClient.loadMyTransactionScene();
    }

    @FXML
    private void onMyBooksButtonClick() throws IOException {
        Scene currentScene = myBooksLink.getScene();

        if (currentScene.getRoot().getId() != null && currentScene.getRoot().getId().equals("myBooksRoot")) {
            return;
        }
        MainClient.loadMyBooksScene();
    }

    @FXML
    private void onLibraryButtonClick() throws IOException {
        Scene currentScene = libraryLink.getScene();

        if (currentScene.getRoot().getId() != null && currentScene.getRoot().getId().equals("libraryRoot")) {
            return;
        }
        MainClient.loadLibraryScene();
    }

    @FXML
    private void onLogoutButtonClick() throws IOException {
        MainClient.loadAuthScene();
    }

    @FXML
    private void onAdminPanelButtonClick() throws IOException {
        MainClient.loadAdminPanelScene();
    }

    @FXML
    public void onHelpButtonClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the Application");
        alert.setHeaderText("Library Management System");
        alert.setContentText("""
                This application is a library management system that allows users to view, borrow, and return books, as well as track their borrowed books.

                In the 'My Collections' tab, you will see a list of all the books you have borrowed, with details such as title and author.
                In the 'Library' tab, you will find a list of all available books that can be borrowed.
                Double-click a book you're interested in to see more details, including its author, ISBN, and other attributes.
                If you haven't borrowed the book yet, you will see the 'Borrow' button. If you've already borrowed it, you will see the 'Return' button to return it to the library.\s
                Additionally, there is a 'My Transactions' button where you can track all your borrowing and returning transactions.
                There is also a 'Settings' button that allows you to update your name or email address.
               \s""");
        alert.showAndWait();
    }
}