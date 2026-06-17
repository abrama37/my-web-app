package com.myapp.db;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:meals.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDatabase() throws Exception {
        InputStream is = DatabaseManager.class
                .getClassLoader()
                .getResourceAsStream("schema.sql");

        String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }

        System.out.println("Database initialized.");
    }
}
