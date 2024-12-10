package com.example.client.controller;

import com.example.client.MainClient;
import com.example.client.dto.TransactionsDTO;
import com.example.client.service.AlertService;
import com.example.client.service.ManageTransactionsService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageTransactionsController {

    @FXML public Button searchButton;
    @FXML ImageView backgroundImage;
    @FXML private TextField searchField;
    @FXML private TableView<TransactionsDTO> transactionsTable;
    @FXML private TableColumn<TransactionsDTO, Integer> transactionIdColumn, userIdColumn, bookIdColumn;
    @FXML private TableColumn<TransactionsDTO, String> actionColumn, dateColumn;

    private List<TransactionsDTO> transactions;
    private final ObservableList<TransactionsDTO> transactionsList = FXCollections.observableArrayList();
    private final ManageTransactionsService manageTransactionsService = new ManageTransactionsService();

    @FXML
    public void initialize() {
        initializeTableColumns();
        loadTransactions();
        transactionsTable.setOnMouseClicked(this::onTableClick);
        setupSearchField();
    }

    private void initializeTableColumns() {
        transactionIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTransactionId()).asObject());
        userIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserId().getId()).asObject());
        bookIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBookId().getId()).asObject());
        actionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAction()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
    }

    private void loadTransactions() {
        try {
            transactionsList.setAll(manageTransactionsService.getAllTransactions());
            transactions = new ArrayList<>(transactionsList);
            transactionsTable.setItems(transactionsList);
        } catch (Exception e) {
            AlertService.showError("Error", "Failed to load transactions.");
        }
    }

    @FXML
    public void onSearchButtonClick() {
        String searchText = searchField.getText();

        List<TransactionsDTO> filteredTransactions = manageTransactionsService.searchTransactions(transactions, searchText);

        transactionsTable.getItems().clear();
        transactionsTable.getItems().addAll(filteredTransactions);
    }

    private void filterTransactionsBySearch(String searchText) {
        List<TransactionsDTO> filteredTransactions = manageTransactionsService.searchTransactions(transactions, searchText);
        transactionsList.clear();
        transactionsList.addAll(filteredTransactions);
    }

    private void setupSearchField() {
        searchField.setOnKeyReleased(_ -> filterTransactionsBySearch(searchField.getText()));
    }


    private void onTableClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            onDeleteTransactionClick();
        }
    }

    @FXML
    public void onDeleteTransactionClick() {
        TransactionsDTO selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            AlertService.showError("Error", "No transaction selected.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete the transaction with ID: " + selectedTransaction.getTransactionId() + "?");
        alert.setContentText("This action cannot be undone.");

        Image image = new Image(getClass().getResource("/com/example/client/images/transaction.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        alert.setGraphic(imageView);

        Optional<ButtonType> confirmed = alert.showAndWait();

        if (confirmed.isPresent() && confirmed.get() == ButtonType.OK) {
            deleteTransaction(selectedTransaction);
        }
    }

    private void deleteTransaction(TransactionsDTO transaction) {
        try {
            boolean success = manageTransactionsService.deleteTransaction(transaction.getTransactionId());
            if (success) {
                transactionsList.remove(transaction);
                AlertService.showInfo("Success", "Transaction deleted successfully.");
            } else {
                AlertService.showError("Error", "Failed to delete transaction.");
            }
        } catch (Exception e) {
            AlertService.showError("Error", "An error occurred while deleting the transaction.");
        }
    }

    @FXML
    public void onBackButtonClick() throws IOException {
        MainClient.loadAdminPanelScene();
    }
}
