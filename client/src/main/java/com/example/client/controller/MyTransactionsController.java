package com.example.client.controller;

import com.example.client.MainClient;
import com.example.client.dto.TransactionsDTO;
import com.example.client.dto.UserDTO;
import com.example.client.service.AlertService;
import com.example.client.service.MyTransactionsService;
import com.example.client.service.UserInfoService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.List;

public class MyTransactionsController {

    @FXML public Label statusLabel;
    @FXML public ImageView backgroundImage;
    @FXML private TextField searchField;
    @FXML private TableView<TransactionsDTO> transactionsTable;
    @FXML private TableColumn<TransactionsDTO, Integer> transactionIdColumn, userIdColumn, bookIdColumn;
    @FXML private TableColumn<TransactionsDTO, String> actionColumn, dateColumn;

    private List<TransactionsDTO> transactions;
    private final ObservableList<TransactionsDTO> transactionsList = FXCollections.observableArrayList();
    private final MyTransactionsService myTransactionsService = new MyTransactionsService();
    private final UserInfoService userInfoService = new UserInfoService();

    @FXML
    public void initialize() {
        initializeTableColumns();
        loadTransactionsFromDatabase();
        setupSearchField();
    }

    private void initializeTableColumns() {
        transactionIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTransactionId()).asObject());
        userIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserId().getId()).asObject());
        bookIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBookId().getId()).asObject());
        actionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAction()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
    }

    private void loadTransactionsFromDatabase() {
        try {
            UserDTO user = userInfoService.fetchUserInfo();
            if (user == null) {
                throw new IllegalStateException("User information could not be fetched.");
            }

            transactionsList.clear();
            List<TransactionsDTO> transactions = myTransactionsService.fetchUserTransactions(user.getId());
            this.transactions = myTransactionsService.fetchUserTransactions(user.getId());
            transactionsList.addAll(transactions);
            transactionsTable.setItems(transactionsList);
        } catch (Exception e) {
            AlertService.showError("Error", "Failed to load transactions. Please try again.");
        }
    }

    @FXML
    public void onSearchButtonClick() {
        String searchText = searchField.getText();

        List<TransactionsDTO> filteredTransactions = myTransactionsService.searchTransactions(transactions, searchText);

        transactionsTable.getItems().clear();
        transactionsTable.getItems().addAll(filteredTransactions);
    }

    private void filterTransactionsBySearch(String searchText) {
        List<TransactionsDTO> filteredTransactions = myTransactionsService.searchTransactions(transactions, searchText);
        transactionsList.clear();
        transactionsList.addAll(filteredTransactions);
    }

    private void setupSearchField() {
        searchField.setOnKeyReleased(_ -> filterTransactionsBySearch(searchField.getText()));
    }

    @FXML
    public void onBackButtonClick() throws IOException {
        MainClient.loadUserAccountScene();
    }
}
