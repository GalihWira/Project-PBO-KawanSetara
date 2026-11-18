package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
            FROM Lowongan l
            JOIN Perusahaan p 
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
    public List<Lowongan> getAllLowonganAdmin() {
        List<Lowongan> daftarLowongan = new ArrayList<>();
        String sql = """
            SELECT 
                l.id_lowongan,
                l.id_admin,
                l.nama_pekerjaan,
                l.deskripsi,
                l.lokasi,
                l.gaji,
                l.jenis_lowongan,
                l.persyaratan,
                l.status,
                p.id_perusahaan,
                p.nama_perusahaan,
                p.alamat,
                (SELECT COUNT(*) FROM Lamaran lm WHERE lm.id_lowongan = l.id_lowongan) AS jumlah_pelamar
            FROM Lowongan l
            JOIN Perusahaan p ON l.id_perusahaan = p.id_perusahaan
            ORDER BY l.id_lowongan DESC
        """;

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Perusahaan perusahaan = new Perusahaan(
                    rs.getInt("id_perusahaan"),
                    rs.getString("nama_perusahaan"),
                    rs.getString("alamat"),
                    null, null, null
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
                lowongan.setIdAdmin(rs.getInt("id_admin"));
                lowongan.setPersyaratan(rs.getString("persyaratan"));
                lowongan.setJumlahPelamar(rs.getInt("jumlah_pelamar"));

                daftarLowongan.add(lowongan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarLowongan;
    }

    public List<Perusahaan> getAllPerusahaan() {
        List<Perusahaan> list = new ArrayList<>();
        String sql = "SELECT id_perusahaan, nama_perusahaan, alamat FROM Perusahaan ORDER BY nama_perusahaan ASC";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Perusahaan(
                    rs.getInt("id_perusahaan"),
                    rs.getString("nama_perusahaan"),
                    rs.getString("alamat"),
                    null, null, null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<Integer, String> getAllKeahlian() {
        Map<Integer, String> map = new LinkedHashMap<>();
        String sql = "SELECT id_keahlian, nama_keahlian FROM Keahlian ORDER BY nama_keahlian ASC";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getInt("id_keahlian"), rs.getString("nama_keahlian"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public boolean tambahLowongan(Lowongan lowongan, List<Integer> idKeahlianList) {
        String sqlLowongan = "INSERT INTO Lowongan (id_admin, id_perusahaan, nama_pekerjaan, deskripsi, gaji, lokasi, jenis_lowongan, persyaratan, status) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlKeahlian = "INSERT INTO Lowongan_Keahlian (id_lowongan, id_keahlian) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = KoneksiDB.getConnection();
            conn.setAutoCommit(false);

            int generatedIdLowongan = -1;
            try (PreparedStatement ps = conn.prepareStatement(sqlLowongan, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, lowongan.getIdAdmin());
                ps.setInt(2, lowongan.getPerusahaan().getIdPerusahaan());
                ps.setString(3, lowongan.getNamaPekerjaan());
                ps.setString(4, lowongan.getDeskripsi());
                ps.setDouble(5, lowongan.getGaji());
                ps.setString(6, lowongan.getLokasi());
                ps.setString(7, lowongan.getJenisLowongan());
                ps.setString(8, lowongan.getPersyaratan());
                ps.setString(9, lowongan.getStatus());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        generatedIdLowongan = keys.getInt(1);
                    }
                }
            }

            if (generatedIdLowongan != -1 && idKeahlianList != null && !idKeahlianList.isEmpty()) {
                try (PreparedStatement psKeahlian = conn.prepareStatement(sqlKeahlian)) {
                    for (Integer idKeahlian : idKeahlianList) {
                        psKeahlian.setInt(1, generatedIdLowongan);
                        psKeahlian.setInt(2, idKeahlian);
                        psKeahlian.addBatch();
                    }
                    psKeahlian.executeBatch();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    
    public boolean hapusLowongan(int idLowongan) {
        String sql = "DELETE FROM Lowongan WHERE id_lowongan = ?";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLowongan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Keahlian> getKeahlianByLowongan(int idLowongan) throws SQLException {
        List<Keahlian> list = new ArrayList<>();

        String sql = """
            SELECT k.id_keahlian, k.nama_keahlian
            FROM Lowongan_Keahlian lk
            JOIN Keahlian k ON lk.id_keahlian = k.id_keahlian
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