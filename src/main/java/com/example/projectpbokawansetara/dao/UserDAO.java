package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.projectpbokawansetara.model.Admin;
import com.example.projectpbokawansetara.model.Pelamar;
import com.example.projectpbokawansetara.model.User;

public class UserDAO {

    public User cekLogin(String email, String password) throws SQLException {

        String sqlPelamar = """
            SELECT *
            FROM Pelamar
            WHERE email = ? AND password = ?
        """;

        try (
            Connection conn = KoneksiDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlPelamar)
        ) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Date tanggal = rs.getDate("tanggal_lahir");

                    return new Pelamar(
                        rs.getInt("id_pelamar"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("no_telepon"),
                        tanggal != null ? tanggal.toLocalDate() : null,
                        rs.getString("pendidikan"),
                        null,
                        rs.getString("ijazah"),
                        rs.getString("surat_ket_disabilitas"),
                        rs.getString("cv")
                    );
                }
            }
        }

        String sqlAdmin = """
            SELECT *
            FROM Admin
            WHERE email = ? AND password = ?
        """;

        try (
            Connection conn = KoneksiDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlAdmin)
        ) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                        rs.getInt("id_admin"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                }
            }
        }

        return null;
    }
}
