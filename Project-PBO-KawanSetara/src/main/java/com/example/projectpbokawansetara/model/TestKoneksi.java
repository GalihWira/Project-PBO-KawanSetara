package com.example.projectpbokawansetara.model;

import java.sql.Connection;
import java.sql.SQLException;

public class TestKoneksi {
    public static void main(String[] args) {
        try(Connection conn = KoneksiDB.getConnection()){
            System.out.println("Koneksi ke database berhasil!");
        }catch(SQLException e){
            System.out.println("Koneksi ke database!" + e.getMessage());
        }
    }
}
