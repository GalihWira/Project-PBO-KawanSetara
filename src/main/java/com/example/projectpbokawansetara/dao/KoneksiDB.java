package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDB {
    private static final String URL = "jdbc:mysql://localhost:3306/pbo_kawansetara";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // masukkan password masing-masing

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
