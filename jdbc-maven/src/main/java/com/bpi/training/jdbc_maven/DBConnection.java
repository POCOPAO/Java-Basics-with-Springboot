package com.bpi.training.jdbc_maven;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {



    public static void main(String[] args) {

        // Replace these with your actual database details
        String url = "jdbc:postgresql://localhost:5432/training_db";
        String username = "postgres";
        String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected successfully");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
