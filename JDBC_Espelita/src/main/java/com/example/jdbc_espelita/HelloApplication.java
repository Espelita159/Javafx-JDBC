package com.example.jdbc_espelita;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        // Connect To Database
//        MySQLConnection.getConnection();

        // Create The Table

        CreateTable table = new CreateTable();
        table.createTable();

        // Load Login View
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 300);

        scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
        stage.setTitle("Login!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}