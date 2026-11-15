package com.example.projectpbokawansetara.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class KoneksiDB {
    private static final String FILE_CONFIG = "db.properties";

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties config = new Properties();
        try (InputStream in = new FileInputStream(FILE_CONFIG)) {
            config.load(in);
        } catch (IOException e) {
            System.err.println("db.properties tidak ditemukan, pakai pengaturan default (root tanpa password).");
        }
        URL = config.getProperty("db.url", "jdbc:mysql://localhost:3306/pbo_kawansetara");
        USER = config.getProperty("db.user", "root");
        PASSWORD = config.getProperty("db.password", "");
    }

    private KoneksiDB() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}