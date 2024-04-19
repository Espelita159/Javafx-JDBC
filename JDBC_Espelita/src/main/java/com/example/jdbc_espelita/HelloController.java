package com.example.jdbc_espelita;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class HelloController {
    @FXML
    public Button btnLogout, btnSubmit, btnRegister;
    public VBox pnLogin;
    public ColorPicker cpPicker;
    @FXML
    private TextField txtUser, registerUserName;
    @FXML
    private PasswordField txtPass, registerPassword;


    @FXML
    protected void onRegisterClick() throws IOException {
        VBox parent = pnLogin;
        Parent scene = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        parent.getChildren().clear();
        parent.getChildren().add(scene);
    }

    @FXML
    protected void onSubmitClick() throws IOException {
        String regUser = registerUserName.getText();
        String regPass = registerPassword.getText();

        try (Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement("Insert into users (username, password) VALUES (?, ?)")) {

            String inputUsername = regUser;
            String inputPassword = regPass;

            statement.setString(1, inputUsername);
            statement.setString(2, inputPassword);

            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows inserted: " + rowsUpdated);

            AnchorPane p = (AnchorPane) btnSubmit.getParent();
            Parent scene = FXMLLoader.load(getClass().getResource("login-view.fxml"));
            p.getChildren().clear();
            p.getChildren().add(scene);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    protected void onLoginClick() throws IOException {
        String username = txtUser.getText();
        String password = txtPass.getText();

        boolean isValidCredentials = false;

        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "SELECT * FROM users";

            ResultSet res = statement.executeQuery(query);

            while (res.next()) {
                String user =  res.getString("username");
                String pass = res.getString("password");

                if (username.equals(user) && password.equals(pass)) {
                    isValidCredentials = true;
                    break;
                }
            }

            if (isValidCredentials) {
                AnchorPane p = (AnchorPane) pnLogin.getParent();
                p.getScene().getStylesheets().clear();
                p.getScene().getStylesheets().add(getClass().getResource("user1.css").toExternalForm());

                Parent scene = FXMLLoader.load(getClass().getResource("home-view.fxml"));
                scene.prefHeight(p.getScene().getHeight());
                scene.prefWidth(p.getScene().getWidth());
                p.getChildren().clear();
                p.getChildren().add(scene);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password!");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @FXML
    protected void onLogoutClick() throws IOException {
        Color color = cpPicker.getValue();
        String cssColor = "rgb(" +
                (int)(color.getRed() * 255) + "," +
                (int)(color.getGreen() * 255) + "," +
                (int)(color.getBlue() * 255) +
                ")";

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(getClass().getResource("user1.css").getPath(),true));
            bw.write(".button { -fx-background-color: " + cssColor + "; }");
            bw.newLine();
            bw.close();
        } catch (IOException e) {
        }
        AnchorPane p = (AnchorPane) btnLogout.getParent();
        Parent scene = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        p.getChildren().clear();
        p.getChildren().add(scene);
    }
}