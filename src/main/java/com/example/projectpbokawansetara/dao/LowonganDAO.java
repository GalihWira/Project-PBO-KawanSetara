package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.projectpbokawansetara.model.Keahlian;
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
                l.durasi_kerja,
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

                lowongan.setDurasiKerja(rs.getString("durasi_kerja"));

                lowongan.setDaftarKeahlian(getKeahlianByLowongan(lowongan.getIdLowongan()));

                daftarLowongan.add(lowongan);
            }
        }

        return daftarLowongan;
    }

    public List<Keahlian> getKeahlianByLowongan(int idLowongan) throws SQLException {
        List<Keahlian> list = new ArrayList<>();

        String sql = """
            SELECT k.id_keahlian, k.nama_keahlian
            FROM lowongan_keahlian lk
            JOIN keahlian k ON lk.id_keahlian = k.id_keahlian
            WHERE lk.id_lowongan = ?
        """;

        try (
            Connection conn = KoneksiDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, idLowongan);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Keahlian keahlian = new Keahlian();
                keahlian.setIdKeahlian(rs.getInt("id_keahlian"));
                keahlian.setNamaKeahlian(rs.getString("nama_keahlian"));
                list.add(keahlian);
            }
        }

        return list;
    }
}