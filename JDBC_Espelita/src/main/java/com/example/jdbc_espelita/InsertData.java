package com.example.jdbc_espelita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertData {
    public static void main(String[] args) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("Insert into users (username, password) VALUES (?,?)")) {

            String name = "Raphael Espelita";
            String email = "raphael.espelita@cit.edu";

            statement.setString(1, name);
            statement.setString(2, email);

            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows inserted: " + rowsUpdated);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

