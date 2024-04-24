package com.example.jdbc_espelita;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class HelloController {
    private static String loggedInUsername;

    @FXML
    public Button btnLogout, btnSubmit, btnRegister, btnDeleteAll, btnDisplay;
    public VBox pnLogin;
    public ColorPicker cpPicker;
    @FXML
    private TextField txtUser, registerUserName, updateUsername, updatePassword, registerFullname;
    @FXML
    private PasswordField txtPass, registerPassword;
    @FXML
    private Label displayFullName;


    @FXML
    protected void onDisplayClick() {
        String loggedUser = getLoggedInUsername();

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("SELECT full_name FROM user_profiles WHERE user_id = " +
                     "(SELECT id FROM users WHERE username = ?)")) {

            statement.setString(1, loggedUser);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String fullName = resultSet.getString("full_name");
                displayFullName.setText(fullName);
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("No full name found for the user: " + loggedUser);
                errorAlert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onDeleteAll() throws IOException {
        String loggedUser = getLoggedInUsername();

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("DELETE FROM users WHERE username = ?")) {

            statement.setString(1, loggedUser);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText(null);
                success.setContentText("User data deleted successfully.");
                success.showAndWait();

                loggedInUsername = null;

                AnchorPane p = (AnchorPane) btnDeleteAll.getParent();
                Parent scene = FXMLLoader.load(getClass().getResource("login-view.fxml"));
                p.getChildren().clear();
                p.getChildren().add(scene);


            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("No user found with the username: " + loggedUser);
                errorAlert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void onSetNewPasswordClick() {
        String upPass = updatePassword.getText();
        String loggedUser = getLoggedInUsername();

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("UPDATE users SET password = ? WHERE username = ?")) {

            statement.setString(1, upPass);
            statement.setString(2, loggedUser);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText(null);
                success.setContentText("Password updated Successfully");
                success.showAndWait();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("No user found with the username: " + loggedUser);
                errorAlert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSetNewUsernameClick() {
        String upUser = updateUsername.getText();
        String loggedUser = getLoggedInUsername();

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("UPDATE users SET username = ? WHERE username = ?")) {

            statement.setString(1, upUser);
            statement.setString(2, loggedUser);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText(null);
                success.setContentText("Username updated Successfully");
                success.showAndWait();

                loggedInUsername = upUser;
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("No user found with the username: " + loggedUser);
                errorAlert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onRegisterClick() throws IOException {
        VBox parent = pnLogin;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register-view.fxml"));
        Parent scene = fxmlLoader.load();
        scene.prefHeight(555);
        scene.prefWidth(335);

        parent.getChildren().clear();
        parent.getChildren().add(scene);
    }


    @FXML
    protected void onSubmitClick() throws IOException {
        String regUser = registerUserName.getText();
        String regPass = registerPassword.getText();
        String regFullName = registerFullname.getText();

        try (Connection c = MySQLConnection.getConnection()) {
            c.setAutoCommit(false);

            try (PreparedStatement insertUserStatement = c.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement insertProfileStatement = c.prepareStatement("INSERT INTO user_profiles (user_id, full_name) VALUES (?, ?)")) {

                insertUserStatement.setString(1, regUser);
                insertUserStatement.setString(2, regPass);
                int rowsInserted = insertUserStatement.executeUpdate();

                ResultSet generatedKeys = insertUserStatement.getGeneratedKeys();
                int userId = 0;
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                }

                insertProfileStatement.setInt(1, userId);
                insertProfileStatement.setString(2, regFullName);
                insertProfileStatement.executeUpdate();

                c.commit();

                AnchorPane p = (AnchorPane) btnSubmit.getParent();
                Parent scene = FXMLLoader.load(getClass().getResource("login-view.fxml"));
                p.getChildren().clear();
                p.getChildren().add(scene);
            } catch (SQLException e) {
                c.rollback();
                e.printStackTrace();
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException | IOException e) {
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
                    loggedInUsername = username;
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

    public String getLoggedInUsername() {
        return loggedInUsername;
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