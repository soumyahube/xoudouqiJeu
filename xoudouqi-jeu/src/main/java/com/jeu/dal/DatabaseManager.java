package com.jeu.dal;

import java.sql.*;

public class DatabaseManager {
    public  final static String DB_URL = "jdbc:sqlite:xoudougi.db";
    
    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            String sql = "CREATE TABLE IF NOT EXISTS joueurs (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "username TEXT NOT NULL UNIQUE," +
                         "password TEXT NOT NULL," +
                         "score INTEGER DEFAULT 0)";
            
            stmt.execute(sql);
            System.out.println();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}