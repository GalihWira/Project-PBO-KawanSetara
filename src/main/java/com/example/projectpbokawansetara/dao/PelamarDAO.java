package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class PelamarDAO {

    public boolean simpanPelamar(
        String nama,
        String email,
        String password
    ) throws SQLException {

        String sql = """
            INSERT INTO Pelamar (nama, email, password)
            VALUES (?, ?, ?)
        """;

        String passwordHash = BCrypt.hashpw(
            password,
            BCrypt.gensalt(12)
        );

        try (
            Connection conn = KoneksiDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, nama);
            stmt.setString(2, email);
            stmt.setString(3, passwordHash);

            return stmt.executeUpdate() > 0;
        }
    }
}
