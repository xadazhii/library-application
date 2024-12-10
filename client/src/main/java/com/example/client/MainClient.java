package com.example.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainClient extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        loadAuthScene();
    }

    public static void loadAuthScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Library - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadUserAccountScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("user-account.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Library - User Account");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadMyBooksScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("my-books.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Library - My Books");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadLibraryScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("library.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Library - Library Section");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadAdminPanelScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("admin-panel.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Library - Admin Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadManageBooksScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("manage-books.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Library - Manage Books");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadManageUsersScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("manage-users.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Library - Manage Users");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadManageTransactionsScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("manage-transactions.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Library - Transactions");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadMyTransactionScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("my-transactions.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Library - Transactions");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
