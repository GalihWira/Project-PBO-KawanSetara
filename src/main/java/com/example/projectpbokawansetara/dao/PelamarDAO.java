package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.example.projectpbokawansetara.dao.KoneksiDB;
import com.example.projectpbokawansetara.model.Keahlian;
import com.example.projectpbokawansetara.model.Pelamar;

public class PelamarDAO {

    public boolean simpanPelamar(Pelamar pelamar, List<String> daftarDisabilitas) throws SQLException {

        String sqlPelamar = """
                    INSERT INTO Pelamar
                        (nama, email, password, no_telepon, usia, tanggal_lahir,
                         pendidikan, ijazah, surat_ket_disabilitas, cv)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String sqlDisabilitas = """
                    INSERT INTO Pelamar_Disabilitas (id_pelamar, id_disabilitas)
                    SELECT ?, id_disabilitas
                    FROM Jenis_Disabilitas
                    WHERE nama_disabilitas = ?
                """;

        String passwordHash = BCrypt.hashpw(pelamar.getPassword(), BCrypt.gensalt(12));
        LocalDate tanggalLahir = pelamar.getTanggalLahir();

        try (Connection conn = KoneksiDB.getConnection()) {
            conn.setAutoCommit(false);

            try {
                int idPelamar;

                try (PreparedStatement stmt = conn.prepareStatement(sqlPelamar, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, pelamar.getNama());
                    stmt.setString(2, pelamar.getEmail());
                    stmt.setString(3, passwordHash);
                    stmt.setString(4, pelamar.getNoTelepon());
                    stmt.setInt(5, Period.between(tanggalLahir, LocalDate.now()).getYears());
                    stmt.setDate(6, Date.valueOf(tanggalLahir));
                    stmt.setString(7, pelamar.getPendidikan());
                    stmt.setString(8, pelamar.getIjazah());
                    stmt.setString(9, pelamar.getSuratKeteranganDisabilitas());
                    stmt.setString(10, pelamar.getCv());
                    stmt.executeUpdate();

                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        rs.next();
                        idPelamar = rs.getInt(1);
                    }
                }

                try (PreparedStatement stmt = conn.prepareStatement(sqlDisabilitas)) {
                    for (String namaDisabilitas : daftarDisabilitas) {
                        stmt.setInt(1, idPelamar);
                        stmt.setString(2, namaDisabilitas);
                        stmt.executeUpdate();
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<Keahlian> getKeahlianByPelamar(int idPelamar) throws SQLException {
        List<Keahlian> list = new ArrayList<>();

        String sql = """
                    SELECT k.id_keahlian, k.nama_keahlian
                    FROM Pelamar_Keahlian pk
                    JOIN Keahlian k ON pk.id_keahlian = k.id_keahlian
                    WHERE pk.id_pelamar = ?
                """;

        try (
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPelamar);
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

    public Pelamar getPelamarById(int idPelamar) throws SQLException {
        String sql = """
                    SELECT id_pelamar, nama, email, no_telepon, tanggal_lahir,
                        pendidikan, ijazah, surat_ket_disabilitas, cv, foto
                    FROM Pelamar
                    WHERE id_pelamar = ?
                """;

        try (
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPelamar);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Date tanggal = rs.getDate("tanggal_lahir");

                return new Pelamar(
                        rs.getInt("id_pelamar"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        null,
                        rs.getString("no_telepon"),
                        tanggal != null ? tanggal.toLocalDate() : null,
                        rs.getString("pendidikan"),
                        null,
                        rs.getString("ijazah"),
                        rs.getString("surat_ket_disabilitas"),
                        rs.getString("cv"),
                        rs.getString("foto"));
            }
        }
    }

    public List<Integer> getIdDisabilitasPelamar(int idPelamar) throws SQLException {
        List<Integer> daftarId = new ArrayList<>();

        String sql = """
                    SELECT id_disabilitas
                    FROM Pelamar_Disabilitas
                    WHERE id_pelamar = ?
                """;

        try (
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPelamar);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    daftarId.add(rs.getInt("id_disabilitas"));
                }
            }
        }

        return daftarId;
    }

    public boolean perbaruiProfil(Pelamar pelamar, List<Integer> daftarIdDisabilitas) throws SQLException {
        String sqlPelamar = """
                UPDATE Pelamar
                SET nama = ?, no_telepon = ?, tanggal_lahir = ?, usia = ?, pendidikan = ?,
                    ijazah = ?, surat_ket_disabilitas = ?, cv = ?, foto = ?
                WHERE id_pelamar = ?
            """;

        String sqlHapusDisabilitas = "DELETE FROM Pelamar_Disabilitas WHERE id_pelamar = ?";

        String sqlTambahDisabilitas = """
                    INSERT INTO Pelamar_Disabilitas (id_pelamar, id_disabilitas)
                    VALUES (?, ?)
                """;

        LocalDate tanggalLahir = pelamar.getTanggalLahir();

        try (Connection conn = KoneksiDB.getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement stmt = conn.prepareStatement(sqlPelamar)) {
                    stmt.setString(1, pelamar.getNama());
                    stmt.setString(2, pelamar.getNoTelepon());
                    stmt.setDate(3, Date.valueOf(tanggalLahir));
                    stmt.setInt(4, Period.between(tanggalLahir, LocalDate.now()).getYears());
                    stmt.setString(5, pelamar.getPendidikan());
                    stmt.setString(6, pelamar.getIjazah());
                    stmt.setString(7, pelamar.getSuratKeteranganDisabilitas());
                    stmt.setString(8, pelamar.getCv());
                    stmt.setString(9, pelamar.getFoto());
                    stmt.setInt(10, pelamar.getIdPengguna());
                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = conn.prepareStatement(sqlHapusDisabilitas)) {
                    stmt.setInt(1, pelamar.getIdPengguna());
                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = conn.prepareStatement(sqlTambahDisabilitas)) {
                    for (int idDisabilitas : daftarIdDisabilitas) {
                        stmt.setInt(1, pelamar.getIdPengguna());
                        stmt.setInt(2, idDisabilitas);
                        stmt.executeUpdate();
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<String> getNamaDisabilitasPelamar(int idPelamar) throws SQLException {
        List<String> daftarNama = new ArrayList<>();

        String sql = """
                    SELECT jd.nama_disabilitas
                    FROM Pelamar_Disabilitas pd
                    JOIN Jenis_Disabilitas jd ON pd.id_disabilitas = jd.id_disabilitas
                    WHERE pd.id_pelamar = ?
                    ORDER BY jd.nama_disabilitas
                """;

        try (
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPelamar);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    daftarNama.add(rs.getString("nama_disabilitas"));
                }
            }
        }

        return daftarNama;
    }
}