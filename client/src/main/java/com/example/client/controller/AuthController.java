package com.example.client.controller;

import com.example.client.MainClient;
import com.example.client.service.AuthService;
import com.example.client.service.AlertService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class AuthController {

    @FXML public ImageView backgroundImage, usernameIcon, passwordIcon, signupUsernameIcon, signupEmailIcon, signupPasswordIcon;
    @FXML public Pane formBackground;
    @FXML private Tab loginTab;
    @FXML private TextField usernameField, emailField, loginUsernameField;
    @FXML private PasswordField passwordField, loginPasswordField;
    @FXML private TabPane tabPane;

    @FXML
    private void initialize() {
        addEnterKeyListener(usernameField, this::onRegisterButtonClick);
        addEnterKeyListener(emailField, this::onRegisterButtonClick);
        addEnterKeyListener(passwordField, this::onRegisterButtonClick);
        addEnterKeyListener(loginUsernameField, this::onLoginButtonClick);
        addEnterKeyListener(loginPasswordField, this::onLoginButtonClick);
    }

    private void addEnterKeyListener(TextField textField, Runnable action) {
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                action.run();
            }
        });
    }

    @FXML
    private void onRegisterButtonClick() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        String response = AuthService.register(username, email, password);
        handleAuthResponse(response, true);
    }

    @FXML
    private void onLoginButtonClick() {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        String response = AuthService.login(username, password);
        handleAuthResponse(response, false);
    }

    private void handleAuthResponse(String response, boolean isRegistration) {
        if (response != null && response.contains("successful")) {
            handleSuccessResponse(isRegistration);
        } else {
            handleFailureResponse(response, isRegistration);
        }
    }

    private void handleSuccessResponse(boolean isRegistration) {
        if (isRegistration) {
            Platform.runLater(() -> {
                AlertService.showInfo("Registration Successful", "Registration successful! Redirecting to login.");
                onSwitchToLoginTab();
            });
        } else {
            onLoadUserAccountScene();
        }
    }

    @FXML
    private void onSwitchToLoginTab() {
        if (tabPane != null && loginTab != null) {
            tabPane.getSelectionModel().select(loginTab);
        }
    }

    @FXML
    private void onLoadUserAccountScene() {
        try {
            MainClient.loadUserAccountScene();
        } catch (Exception e) {
            AlertService.showError("Error", "Failed to load account scene: " + e.getMessage());
        }
    }

    private void handleFailureResponse(String response, boolean isRegistration) {
        String errorMessage = (response != null) ? response : "Unknown error occurred.";
        String errorTitle = isRegistration ? "Registration Error" : "Login Error";
        AlertService.showError(errorTitle, errorMessage);
    }
}
