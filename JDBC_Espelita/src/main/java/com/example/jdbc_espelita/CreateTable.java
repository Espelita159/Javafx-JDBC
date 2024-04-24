package com.example.jdbc_espelita;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public void createTable() {
        try (Connection c = MySQLConnection.getConnection(); Statement statement = c.createStatement()) {
            // Create users table
            String usersQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," +
                    "password VARCHAR(50) NOT NULL)";
            statement.execute(usersQuery);

            // Create user_profiles table
            String userProfileQuery = "CREATE TABLE IF NOT EXISTS user_profiles (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "full_name VARCHAR(100)," +
                    "CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id))";
            statement.execute(userProfileQuery);

            System.out.println("DB Connection");
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
