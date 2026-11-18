package com.example.projectpbokawansetara.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.example.projectpbokawansetara.model.Admin;
import com.example.projectpbokawansetara.model.Pelamar;
import com.example.projectpbokawansetara.model.User;

public class UserDAO {

    public User cekLogin(String email, String password) throws SQLException {

        String sqlPelamar = """
                    SELECT *
                    FROM Pelamar
                    WHERE email = ?
                """;

        try (
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlPelamar)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    String passwordDB = rs.getString("password");

                    if (!passwordCocok(password, passwordDB)) {
                        return null;
                    }

                    if (!passwordDB.startsWith("$2")) {
                        perbaruiPassword(
                                "Pelamar",
                                "id_pelamar",
                                rs.getInt("id_pelamar"),
                                password);
                    }

                    Date tanggal = rs.getDate("tanggal_lahir");

                    return new Pelamar(
                            rs.getInt("id_pelamar"),
                            rs.getString("nama"),
                            rs.getString("email"),
                            passwordDB,
                            rs.getString("no_telepon"),
                            tanggal != null ? tanggal.toLocalDate() : null,
                            rs.getString("pendidikan"),
                            null,
                            rs.getString("ijazah"),
                            rs.getString("surat_ket_disabilitas"),
                            rs.getString("cv"));
                }
            }
        }

        String sqlAdmin = """
                    SELECT *
                    FROM Admin
                    WHERE email = ?
                """;

        try (
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlAdmin)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    String passwordDB = rs.getString("password");

                    if (!passwordCocok(password, passwordDB)) {
                        return null;
                    }

                    if (!passwordDB.startsWith("$2")) {
                        perbaruiPassword(
                                "Admin",
                                "id_admin",
                                rs.getInt("id_admin"),
                                password);
                    }

                    return new Admin(
                            rs.getInt("id_admin"),
                            rs.getString("nama"),
                            rs.getString("email"),
                            passwordDB);
                }
            }
        }

        return null;
    }

    public boolean emailSudahTerdaftar(String email) throws SQLException {

        String sql = """
                    SELECT email FROM Pelamar WHERE email = ?
                    UNION
                    SELECT email FROM Admin WHERE email = ?
                """;

        try (
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, email);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Admin> getSemuaAdmin() throws SQLException {

        String sql = """
                    SELECT id_admin, nama, email
                    FROM Admin
                    ORDER BY nama
                """;

        List<Admin> daftarAdmin = new ArrayList<>();

        try (
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                daftarAdmin.add(new Admin(
                        rs.getInt("id_admin"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        null));
            }
        }

        return daftarAdmin;
    }

    private boolean passwordCocok(String password, String passwordDB) {

        if (passwordDB == null) {
            return false;
        }

        if (passwordDB.startsWith("$2")) {
            return BCrypt.checkpw(password, passwordDB);
        }

        return password.equals(passwordDB);
    }

    private void perbaruiPassword(
            String tabel,
            String kolomId,
            int id,
            String password) throws SQLException {

        String sql = "UPDATE " + tabel
                + " SET password = ? WHERE " + kolomId + " = ?";

        String passwordHash = BCrypt.hashpw(
                password,
                BCrypt.gensalt(12));

        try (
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, passwordHash);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
}