package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.projectpbokawansetara.model.ItemChecklist;

public class ChecklistDAO {

    public Map<String, List<ItemChecklist>> ambilSemuaKeahlian() throws SQLException {
        Map<String, List<ItemChecklist>> daftarKategori = new LinkedHashMap<>();

        String sql = """
                    SELECT
                        k.nama_kategori,
                        e.id_keahlian,
                        e.nama_keahlian
                    FROM Keahlian e
                    JOIN KategoriKeahlian k
                        ON e.id_kategori = k.id_kategori
                    ORDER BY k.nama_kategori, e.nama_keahlian
                """;

        try (
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String namaKategori = rs.getString("nama_kategori");

                ItemChecklist item = new ItemChecklist(
                        rs.getInt("id_keahlian"),
                        rs.getString("nama_keahlian"));

                daftarKategori
                        .computeIfAbsent(namaKategori, key -> new ArrayList<>())
                        .add(item);
            }
        }
        return daftarKategori;
    }

    public Set<Integer> ambilKeahlianPelamar(int idPelamar) throws SQLException {
        Set<Integer> keahlianDipilih = new LinkedHashSet<>();

        String sql = """
                    SELECT id_keahlian
                    FROM Pelamar_Keahlian
                    WHERE id_pelamar = ?
                """;

        try (
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPelamar);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    keahlianDipilih.add(rs.getInt("id_keahlian"));
                }
            }
        }
        return keahlianDipilih;
    }

    public void simpanKeahlianPelamar(
            int idPelamar,
            Set<Integer> daftarKeahlian) throws SQLException {

        String hapusSql = """
                    DELETE FROM Pelamar_Keahlian
                    WHERE id_pelamar = ?
                """;

        String tambahSql = """
                    INSERT INTO Pelamar_Keahlian (id_pelamar, id_keahlian)
                    VALUES (?, ?)
                """;

        try (Connection conn = KoneksiDB.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement hapusStmt = conn.prepareStatement(hapusSql);
                    PreparedStatement tambahStmt = conn.prepareStatement(tambahSql)) {
                hapusStmt.setInt(1, idPelamar);
                hapusStmt.executeUpdate();

                for (int idKeahlian : daftarKeahlian) {
                    tambahStmt.setInt(1, idPelamar);
                    tambahStmt.setInt(2, idKeahlian);
                    tambahStmt.addBatch();
                }
                tambahStmt.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<ItemChecklist> getAllItemAdmin() {
        List<ItemChecklist> list = new ArrayList<>();
        String sql = "SELECT e.id_keahlian, e.nama_keahlian, e.id_kategori, k.nama_kategori, "
                + "(SELECT COUNT(*) FROM Lowongan_Keahlian lk WHERE lk.id_keahlian = e.id_keahlian) AS jml_lowongan, "
                + "(SELECT COUNT(*) FROM Pelamar_Keahlian pk WHERE pk.id_keahlian = e.id_keahlian) AS jml_pelamar "
                + "FROM Keahlian e "
                + "LEFT JOIN KategoriKeahlian k ON e.id_kategori = k.id_kategori "
                + "ORDER BY e.id_keahlian ASC";

        try (Connection conn = KoneksiDB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String kategori = rs.getString("nama_kategori");
                if (kategori == null)
                    kategori = "Umum";

                list.add(new ItemChecklist(
                        rs.getInt("id_keahlian"),
                        rs.getString("nama_keahlian"),
                        rs.getInt("id_kategori"),
                        kategori,
                        rs.getInt("jml_lowongan"),
                        rs.getInt("jml_pelamar")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllKategori() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT nama_kategori FROM KategoriKeahlian ORDER BY id_kategori ASC";

        try (Connection conn = KoneksiDB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("nama_kategori"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean tambahItem(String namaItem, String namaKategori) {
        String sql = "INSERT INTO Keahlian (nama_keahlian, id_kategori) "
                + "VALUES (?, (SELECT id_kategori FROM KategoriKeahlian WHERE nama_kategori = ? LIMIT 1))";

        try (Connection conn = KoneksiDB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, namaItem);
            ps.setString(2, namaKategori);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hapusItem(int idKeahlian) {
        String sql = "DELETE FROM Keahlian WHERE id_keahlian = ?";
        try (Connection conn = KoneksiDB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idKeahlian);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public int hitungPemakaian(int idKeahlian) {
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM Pelamar_Keahlian WHERE id_keahlian = ?) + "
                + "(SELECT COUNT(*) FROM Lowongan_Keahlian WHERE id_keahlian = ?)";

        try (Connection conn = KoneksiDB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idKeahlian);
            ps.setInt(2, idKeahlian);

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