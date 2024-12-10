package com.example.client.controller;

import com.example.client.MainClient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import java.io.IOException;

public class AdminPanelController {

    @FXML public ImageView backgroundImage;
    @FXML public Hyperlink manageUsersLink, manageTransactionsLink, manageBooksLink;
    @FXML public Button helpButton;

    @FXML
    private void openManageUsersPanel() throws IOException {
        MainClient.loadManageUsersScene();
    }

    @FXML
    private void openManageBooksPanel() throws IOException {
        MainClient.loadManageBooksScene();
    }

    @FXML
    private void openManageTransactionsPanel() throws IOException {
        MainClient.loadManageTransactionsScene();
    }

    @FXML
    private void showAdminInfo() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Admin Info");
        alert.setHeaderText("Administrator Features");
        alert.setContentText("""
            As an administrator, you can:
           
            - Manage users (add, update, delete, view)
            - Manage books in the library (add, update, delete)
            - Manage transactions: monitor and oversee all borrowing and returning activities of users
           
            Ensure smooth operation of the library system!
            """);
        alert.showAndWait();
    }

    @FXML
    private void goBack() throws IOException {
        MainClient.loadUserAccountScene();
    }
}
