package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.projectpbokawansetara.model.Lowongan;
import com.example.projectpbokawansetara.model.Perusahaan;

public class LowonganDAO {

    public List<Lowongan> getSemuaLowongan() throws SQLException {
        List<Lowongan> daftarLowongan = new ArrayList<>();

        String sql = """
            SELECT 
                l.id_lowongan,
                l.nama_pekerjaan,
                l.deskripsi,
                l.lokasi,
                l.gaji,
                l.jenis_lowongan,
                l.status,
                p.id_perusahaan,
                p.nama_perusahaan,
                p.alamat
            FROM lowongan l
            JOIN perusahaan p 
                ON l.id_perusahaan = p.id_perusahaan
            WHERE l.status = 'tersedia'
            ORDER BY l.id_lowongan DESC
        """;

        try (
            Connection conn = KoneksiDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
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
                    rs.getString("status")
                );

                daftarLowongan.add(lowongan);
            }
        }

        return daftarLowongan;
    }
}
