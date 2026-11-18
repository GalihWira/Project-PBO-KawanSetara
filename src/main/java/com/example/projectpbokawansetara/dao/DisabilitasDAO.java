package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.projectpbokawansetara.model.JenisDisabilitas;

public class DisabilitasDAO {

    public List<JenisDisabilitas> getAllDisabilitas() {
        List<JenisDisabilitas> list = new ArrayList<>();
        String sql = "SELECT id_disabilitas, nama_disabilitas FROM Jenis_Disabilitas ORDER BY id_disabilitas ASC";

        try (Connection conn = KoneksiDB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new JenisDisabilitas(
                        rs.getInt("id_disabilitas"),
                        rs.getString("nama_disabilitas"),
                        0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean tambahDisabilitas(String namaJenis) {
        String sql = "INSERT INTO Jenis_Disabilitas (nama_disabilitas) VALUES (?)";

        try (Connection conn = KoneksiDB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, namaJenis);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hapusDisabilitas(int idJenisDisabilitas) {
        String sql = "DELETE FROM Jenis_Disabilitas WHERE id_disabilitas = ?";

        try (Connection conn = KoneksiDB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idJenisDisabilitas);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public int hitungPemakaian(int idJenisDisabilitas) {
        String sql = "SELECT COUNT(*) FROM Pelamar_Disabilitas WHERE id_disabilitas = ?";

        try (Connection conn = KoneksiDB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idJenisDisabilitas);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}