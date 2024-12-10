package com.example.client.controller;

import com.example.client.MainClient;
import com.example.client.dto.UserDTO;
import com.example.client.service.AlertService;
import com.example.client.service.ManageUsersService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class ManageUsersController {

    @FXML public Button deleteUserButton,addUserButton;
    @FXML ImageView backgroundImage;
    @FXML private TextField usernameField, passwordField, emailField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TableView<UserDTO> usersTable;
    @FXML private TableColumn<UserDTO, Integer> idColumn;
    @FXML private TableColumn<UserDTO, String> usernameColumn, passwordColumn, emailColumn, roleColumn;

    private final ObservableList<UserDTO> usersList = FXCollections.observableArrayList();
    private final ManageUsersService manageUsersService = new ManageUsersService();

    @FXML
    public void initialize() {
        initializeTableColumns();
        loadUsersFromDatabase();
        usersTable.setOnMouseClicked(this::onTableRowClick);
    }

    private void initializeTableColumns() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getRoleId())));
    }

    private void loadUsersFromDatabase() {
        try {
            usersList.clear();
            usersList.addAll(manageUsersService.onGetAllUsersButtonClick());
            usersTable.setItems(usersList);
        } catch (Exception e) {
            AlertService.showError("Error", "Failed to load users.");
        }
    }

    @FXML
    public void onAddUserButtonClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();
        String role = roleComboBox.getValue();

        if (areFieldsValid(username, password, email, role)) {
            try {
                int roleId = getRoleId(role);
                UserDTO newUser = new UserDTO(username, password, email, roleId);

                if (manageUsersService.onAddUserButtonClick(newUser)) {
                    AlertService.showInfo("Success", "User added successfully.");
                    loadUsersFromDatabase();
                } else {
                    AlertService.showError("Error", "Failed to add user.");
                }
            } catch (Exception e) {
                AlertService.showError("Error", "An error occurred while adding the user.");
            }
        }
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

    private int getRoleId(String role) {
        if ("admin".equalsIgnoreCase(role)) return 1;
        if ("user".equalsIgnoreCase(role)) return 2;
        return -1;
    }

    private void onTableRowClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            UserDTO selectedUser = usersTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                openEditUserDialog(selectedUser);
            }
        }
    }

    private void openEditUserDialog(UserDTO selectedUser) {
        String userInfo = "Username: " + selectedUser.getUsername() + "\n" + "Email: " + selectedUser.getEmail() + "\n" + "Current Role: " + (selectedUser.getRoleId() == 1 ? "admin" : "user");

        Alert alert = createEditRoleDialog(userInfo, selectedUser.getRoleId());
        alert.showAndWait().ifPresent(response -> onRoleUpdateClick(selectedUser, response));
    }

    private Alert createEditRoleDialog(String userInfo, int roleId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edit User Information");
        alert.setHeaderText("Edit the user information");
        alert.setContentText(userInfo + "\n\nChoose new role:");

        Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/example/client/images/user.png")).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        alert.setGraphic(imageView);

        ButtonType adminButton = new ButtonType("Admin");
        ButtonType userButton = new ButtonType("User");
        ButtonType cancelButton = ButtonType.CANCEL;

        if (roleId == 1) {
            alert.getButtonTypes().setAll(userButton, cancelButton);
        } else {
            alert.getButtonTypes().setAll(adminButton, cancelButton);
        }

        return alert;
    }

    private void onRoleUpdateClick(UserDTO selectedUser, ButtonType response) {
        if (response == ButtonType.CANCEL) return;

        if (selectedUser.getId() == 1) {
            AlertService.showError("Error", "Cannot update role of user with ID 1.");
            return;
        }

        int newRoleId = "Admin".equals(response.getText()) ? 1 : 2;
        selectedUser.setRoleId(newRoleId);

        try {
            boolean isUpdated = manageUsersService.onUpdateUserRoleButtonClick(selectedUser, String.valueOf(selectedUser.getRoleId()));
            if (isUpdated) {
                AlertService.showInfo("Success", "User role updated successfully.");
                loadUsersFromDatabase();
            } else {
                AlertService.showError("Error", "Failed to update user role.");
            }
        } catch (Exception e) {
            AlertService.showError("Error", "An error occurred while updating the user role.");
        }
    }

    @FXML
    public void onDeleteUserButtonClick() {
        UserDTO selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            confirmAndDeleteUser(selectedUser);
        } else {
            AlertService.showError("Error", "No user selected.");
        }
    }

    private void confirmAndDeleteUser(UserDTO selectedUser) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete the user: " + selectedUser.getUsername() + "?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteUser(selectedUser);
            }
        });
    }

    private void deleteUser(UserDTO selectedUser) {
        if (selectedUser.getId() == 1) {
            AlertService.showError("Error", "Cannot delete user with ID 1.");
            return;
        }

        try {
            if (manageUsersService.onDeleteUserButtonClick(selectedUser.getUsername(), selectedUser.getId())) {
                usersList.remove(selectedUser);
                AlertService.showInfo("Success", "User deleted successfully.");
            } else {
                AlertService.showError("Error", "Failed to delete user.");
            }
        } catch (Exception e) {
            AlertService.showError("Error", "An error occurred while deleting the user.");
        }
    }

    @FXML
    public void onBackButtonClick() throws IOException {
        MainClient.loadAdminPanelScene();
    }
}