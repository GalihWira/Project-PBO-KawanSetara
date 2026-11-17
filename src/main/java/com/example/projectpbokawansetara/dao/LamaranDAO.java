    package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.projectpbokawansetara.model.Lamaran;
import com.example.projectpbokawansetara.model.Lowongan;
import com.example.projectpbokawansetara.model.Perusahaan;

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
    public List<Lamaran> getRiwayatLamaran(int idPelamar)
            throws SQLException {

        List<Lamaran> daftarLamaran = new ArrayList<>();

        String sql = """
            SELECT
                la.id_lamaran,
                la.tanggal_lamar,
                la.status,
                la.tanggal_diperbarui,
                l.id_lowongan,
                l.nama_pekerjaan,
                l.deskripsi,
                l.lokasi,
                l.gaji,
                l.jenis_lowongan,
                l.status AS status_lowongan,
                p.id_perusahaan,
                p.nama_perusahaan,
                p.alamat
            FROM Lamaran la
            JOIN Lowongan l
                ON la.id_lowongan = l.id_lowongan
            JOIN Perusahaan p
                ON l.id_perusahaan = p.id_perusahaan
            WHERE la.id_pelamar = ?
            ORDER BY la.tanggal_lamar DESC, la.id_lamaran DESC
        """;

        try (
            Connection conn = KoneksiDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, idPelamar);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Perusahaan perusahaan = new Perusahaan(
                        rs.getInt("id_perusahaan"),
                        rs.getString("nama_perusahaan"),
                        rs.getString("alamat"),
                        null,
                        null,
                        null
                    );

                    Lowongan lowongan = new Lowongan(
                        rs.getInt("id_lowongan"),
                        perusahaan,
                        rs.getString("nama_pekerjaan"),
                        rs.getString("deskripsi"),
                        rs.getString("lokasi"),
                        rs.getDouble("gaji"),
                        rs.getString("jenis_lowongan"),
                        rs.getString("status_lowongan")
                    );

                    Lamaran lamaran = new Lamaran(
                        rs.getInt("id_lamaran"),
                        null,
                        lowongan,
                        rs.getDate("tanggal_lamar").toLocalDate(),
                        rs.getString("status"),
                        rs.getTimestamp("tanggal_diperbarui") != null
                            ? rs.getTimestamp("tanggal_diperbarui").toLocalDateTime()
                            : null
                    );

                    daftarLamaran.add(lamaran);
                }
            }
        }

        return daftarLamaran;
    }
}