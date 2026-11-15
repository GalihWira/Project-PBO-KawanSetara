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

public class ChecklistDAO{

    public Map<String, List<ItemChecklist>> ambilSemuaKeahlian() throws SQLException{
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
            ResultSet rs = stmt.executeQuery()
        ) {
            while(rs.next()){
                String namaKategori = rs.getString("nama_kategori");

                ItemChecklist item = new ItemChecklist(
                    rs.getInt("id_keahlian"), 
                    rs.getString("nama_keahlian")
                );

                daftarKategori
                    .computeIfAbsent(namaKategori, key -> new ArrayList<>())
                    .add(item);
            }
        }
        return daftarKategori;
    }

    public Set<Integer> ambilKeahlianPelamar(int idPelamar) throws SQLException{
        Set<Integer> keahlianDipilih = new LinkedHashSet<>();

        String sql = """
            SELECT id_keahlian
            FROM Pelamar_Keahlian
            WHERE id_pelamar = ?
        """;

        try(
            Connection conn = KoneksiDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ){
            stmt.setInt(1, idPelamar);

            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    keahlianDipilih.add(rs.getInt("id_keahlian"));
                }
            }
        }
        return keahlianDipilih;
    }

    public void simpanKeahlianPelamar(
        int idPelamar,
        Set<Integer> daftarKeahlian
    ) throws SQLException{
        
        String hapusSql = """
            DELETE FROM Pelamar_Keahlian
            WHERE id_pelamar = ?
        """;

        String tambahSql = """
            INSERT INTO Pelamar_Keahlian (id_pelamar, id_keahlian)
            VALUES (?, ?)
        """;

        try(Connection conn = KoneksiDB.getConnection()){
            conn.setAutoCommit(false);

            try(
                PreparedStatement hapusStmt = conn.prepareStatement(hapusSql);
                PreparedStatement tambahStmt = conn.prepareStatement(tambahSql)
            ){
                hapusStmt.setInt(1, idPelamar);
                hapusStmt.executeUpdate();

                for(int idKeahlian : daftarKeahlian){
                    tambahStmt.setInt(1, idPelamar);
                    tambahStmt.setInt(2, idKeahlian);
                    tambahStmt.addBatch();
                }
                tambahStmt.executeBatch();
                conn.commit();
            }catch(SQLException e){
                conn.rollback();
                throw e;
            }
        }
    }
}