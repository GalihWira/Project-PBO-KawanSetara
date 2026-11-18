package com.example.projectpbokawansetara.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.projectpbokawansetara.model.Perusahaan;

public class PerusahaanDAO {
    
  public List<Perusahaan> getAllPerusahaan() {
        List<Perusahaan> list = new ArrayList<>();
        String sql = "SELECT p.*, "
                + "(SELECT COUNT(*) FROM Lowongan l WHERE l.id_perusahaan = p.id_perusahaan AND l.status = 'tersedia') AS lowongan_aktif "
                + "FROM Perusahaan p ORDER BY p.id_perusahaan DESC";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Perusahaan p = new Perusahaan(
                        rs.getInt("id_perusahaan"),
                        rs.getString("nama_perusahaan"),
                        rs.getString("alamat"),
                        rs.getString("email"),
                        rs.getString("no_telepon"),
                        rs.getString("deskripsi")
                );
                p.setJumlahLowonganAktif(rs.getInt("lowongan_aktif"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean tambahPerusahaan(Perusahaan p) {
        String sql = "INSERT INTO Perusahaan (nama_perusahaan, alamat, email, no_telepon, deskripsi) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNamaPerusahaan());
            ps.setString(2, p.getAlamat());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getNoTelepon());
            ps.setString(5, p.getDeskripsi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePerusahaan(Perusahaan p) {
        String sql = "UPDATE Perusahaan SET nama_perusahaan=?, alamat=?, email=?, no_telepon=?, deskripsi=? WHERE id_perusahaan=?";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNamaPerusahaan());
            ps.setString(2, p.getAlamat());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getNoTelepon());
            ps.setString(5, p.getDeskripsi());
            ps.setInt(6, p.getIdPerusahaan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hapusPerusahaan(int idPerusahaan) {
        String sql = "DELETE FROM Perusahaan WHERE id_perusahaan = ?";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPerusahaan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
