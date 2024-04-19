package com.example.jdbc_espelita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteData {
    public static void main(String[] args) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("DELETE from users WHERE id = ? RETURNING *")) {

            int idToDelete = 1;
            statement.setInt(1, idToDelete);

            int rowsUpdated = statement.executeUpdate();
            ResultSet res = statement.getResultSet();

            if (res.next()) {
                System.out.println("Name: " + res.getString("name"));
                System.out.println("Email: " + res.getString("email"));
            }

//            System.out.println("Rows deleted: " + rowsUpdated);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

