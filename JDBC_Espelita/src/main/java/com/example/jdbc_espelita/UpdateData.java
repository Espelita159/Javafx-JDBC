package com.example.jdbc_espelita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class UpdateData {
    public static void main(String[] args) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("UPDATE users SET name = ? WHERE id = ?")) {

            int findID = 1;
            String newName = "New Name";
//            String newEmail = "New.Email@cit.edu";


            statement.setString(1, newName);
//            statement.setString(2, newEmail);
            statement.setInt(2, findID);

            int rowsUpdated = statement.executeUpdate();

            System.out.println("Rows updated: " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

