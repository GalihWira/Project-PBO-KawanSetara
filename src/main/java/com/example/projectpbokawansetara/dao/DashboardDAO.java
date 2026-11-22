package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.projectpbokawansetara.model.DashboardStatistik;
import com.example.projectpbokawansetara.model.LamaranTerbaru;

public class DashboardDAO {

    public DashboardStatistik getStatistik() {
        DashboardStatistik stat = new DashboardStatistik();

        try (Connection conn = KoneksiDB.getConnection()) {
            stat.setLowonganTersedia(hitung(conn, "SELECT COUNT(*) FROM Lowongan WHERE status = 'tersedia'"));
            stat.setTotalLowongan(hitung(conn, "SELECT COUNT(*) FROM Lowongan"));
            stat.setLamaranMasuk(hitung(conn, "SELECT COUNT(*) FROM Lamaran"));
            stat.setPerusahaanMitra(hitung(conn, "SELECT COUNT(*) FROM Perusahaan"));
            stat.setPelamarTerdaftar(hitung(conn, "SELECT COUNT(*) FROM Pelamar"));
            stat.setTotalChecklist(hitung(conn, "SELECT COUNT(*) FROM Keahlian"));
            stat.setTotalDisabilitas(hitung(conn, "SELECT COUNT(*) FROM Jenis_Disabilitas"));
            stat.setTotalAdmin(hitung(conn, "SELECT COUNT(*) FROM Admin"));
            stat.setLowonganDitutup(hitung(conn, "SELECT COUNT(*) FROM Lowongan WHERE status = 'tidak tersedia'"));
            stat.setLamaranBelumDitinjau(hitung(conn, "SELECT COUNT(*) FROM Lamaran WHERE status = 'diproses'"));
            stat.setPerusahaanTanpaLowongan(hitung(conn,
                    "SELECT COUNT(*) FROM Perusahaan p WHERE NOT EXISTS ("
                            + "SELECT 1 FROM Lowongan l WHERE l.id_perusahaan = p.id_perusahaan AND l.status = 'tersedia')"));
            stat.setPelamarAktifMelamar(hitung(conn, "SELECT COUNT(DISTINCT id_pelamar) FROM Lamaran"));
        } catch (SQLException e) {
            System.err.println("Gagal memuat statistik dashboard: " + e.getMessage());
        }

        return stat;
    }

    public List<LamaranTerbaru> getLamaranTerbaru() {
        List<LamaranTerbaru> list = new ArrayList<>();
        String sql = "SELECT p.nama, l.nama_pekerjaan, lam.tanggal_lamar, lam.status "
                + "FROM Lamaran lam "
                + "JOIN Pelamar p ON lam.id_pelamar = p.id_pelamar "
                + "JOIN Lowongan l ON lam.id_lowongan = l.id_lowongan "
                + "ORDER BY lam.tanggal_lamar DESC, lam.id_lamaran DESC LIMIT 5";

        try (Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new LamaranTerbaru(
                        rs.getString("nama"),
                        rs.getString("nama_pekerjaan"),
                        rs.getString("tanggal_lamar"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat lamaran terbaru: " + e.getMessage());
        }

        return list;
    }

    private int hitung(Connection conn, String sql) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}