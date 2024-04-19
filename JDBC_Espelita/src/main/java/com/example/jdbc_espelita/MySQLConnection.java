package com.example.jdbc_espelita;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    public static final String URL = "jdbc:mysql://localhost:3306/f3espelita_jdbc";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("DB Connection");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void main(String[] args) {
        getConnection();
    }
}

