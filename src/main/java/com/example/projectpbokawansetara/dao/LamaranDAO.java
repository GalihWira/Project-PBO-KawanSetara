    package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class LamaranDAO {

    public boolean sudahMelamar(int idPelamar, int idLowongan)
            throws SQLException {

        String sql = """
            SELECT COUNT(*) 
            FROM Lamaran
            WHERE id_pelamar = ?
            AND id_lowongan = ?
        """;

        try (
            Connection conn = KoneksiDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, idPelamar);
            stmt.setInt(2, idLowongan);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public boolean simpanLamaran(int idPelamar, int idLowongan)
            throws SQLException {

        String sql = """
            INSERT INTO Lamaran (id_pelamar, id_lowongan, tanggal_lamar)
            VALUES (?, ?, ?)
        """;

        try (
            Connection conn = KoneksiDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, idPelamar);
            stmt.setInt(2, idLowongan);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));

            return stmt.executeUpdate() > 0;
        }
    }
}