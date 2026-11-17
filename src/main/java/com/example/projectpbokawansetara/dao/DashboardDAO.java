package com.example.projectpbokawansetara.dao;

import com.example.projectpbokawansetara.model.DashboardStatistik;
import com.example.projectpbokawansetara.model.LamaranTerbaru;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {
    private Connection conn;

    public DashboardDAO() {
        try {
            this.conn = KoneksiDB.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal terhubung ke database: " + e.getMessage());
        }
    }

  
    public DashboardStatistik getStatistik() {
        DashboardStatistik stat = new DashboardStatistik();
        try {
            stat.setLowonganTersedia(countQuery("SELECT COUNT(*) FROM Lowongan WHERE status = 'tersedia'"));
            stat.setTotalLowongan(countQuery("SELECT COUNT(*) FROM Lowongan"));
            stat.setLamaranMasuk(countQuery("SELECT COUNT(*) FROM Lamaran"));
            stat.setPerusahaanMitra(countQuery("SELECT COUNT(*) FROM Perusahaan"));
            stat.setPelamarTerdaftar(countQuery("SELECT COUNT(*) FROM Pelamar"));
            stat.setTotalChecklist(countQuery("SELECT COUNT(*) FROM Keahlian"));
            stat.setTotalDisabilitas(countQuery("SELECT COUNT(*) FROM Jenis_Disabilitas"));
            stat.setTotalAdmin(countQuery("SELECT COUNT(*) FROM Admin"));
            stat.setLowonganDitutup(countQuery("SELECT COUNT(*) FROM Lowongan WHERE status = 'tidak tersedia'"));
            stat.setLamaranBelumDitinjau(countQuery("SELECT COUNT(*) FROM Lamaran WHERE status = 'diproses'"));
            stat.setPerusahaanTanpaLowongan(countQuery(
                "SELECT COUNT(*) FROM Perusahaan p WHERE NOT EXISTS (" +
                "SELECT 1 FROM Lowongan l WHERE l.id_perusahaan = p.id_perusahaan AND l.status = 'tersedia')"
            ));
            stat.setPelamarAktifMelamar(countQuery("SELECT COUNT(DISTINCT id_pelamar) FROM Lamaran"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stat;
    }

    public List<LamaranTerbaru> getLamaranTerbaru() {
        List<LamaranTerbaru> list = new ArrayList<>();
        String sql = "SELECT p.nama, l.nama_pekerjaan, lam.tanggal_lamar, lam.status " +
                     "FROM Lamaran lam " +
                     "JOIN Pelamar p ON lam.id_pelamar = p.id_pelamar " +
                     "JOIN Lowongan l ON lam.id_lowongan = l.id_lowongan " +
                     "ORDER BY lam.tanggal_lamar DESC, lam.id_lamaran DESC LIMIT 5";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                list.add(new LamaranTerbaru(
                    rs.getString("nama"),
                    rs.getString("nama_pekerjaan"),
                    rs.getString("tanggal_lamar"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private int countQuery(String sql) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}