package com.example.projectpbokawansetara.helper;

import java.time.LocalDate;
import java.time.Period;

public class ValidasiHelper {

    private static final String[] DOMAIN_DIIZINKAN = {
            "gmail.com",
            "yahoo.com",
            "yahoo.co.id",
            "outlook.com",
            "hotmail.com",
            "icloud.com",
            "kawansetara.com"
    };
    private static final long UKURAN_MAKS_FILE = 2 * 1024 * 1024;

    private ValidasiHelper() {
    }

    public static boolean emailValid(String email) {
        if (email == null || email.isEmpty() || email.contains(" ")) {
            return false;
        }

        int posisiAt = email.indexOf('@');
        if (posisiAt <= 0 || posisiAt != email.lastIndexOf('@')) {
            return false;
        }

        String domain = email.substring(posisiAt + 1);
        int posisiTitik = domain.lastIndexOf('.');
        return posisiTitik > 0 && posisiTitik < domain.length() - 1;
    }

    public static boolean domainDiizinkan(String email) {
        String domain = email.substring(email.indexOf('@') + 1).toLowerCase();

        if (domain.endsWith(".ac.id")) {
            return true;
        }

        for (String d : DOMAIN_DIIZINKAN) {
            if (domain.equals(d)) {
                return true;
            }
        }
        return false;
    }

    public static boolean teleponValid(String telepon) {
        if (telepon == null || !telepon.startsWith("08")) {
            return false;
        }
        if (telepon.length() < 10 || telepon.length() > 13) {
            return false;
        }
        return semuaAngka(telepon);
    }

    public static boolean namaValid(String nama) {
        if (nama == null) {
            return false;
        }

        String bersih = nama.trim();
        if (bersih.length() < 3 || bersih.length() > 50) {
            return false;
        }

        for (char c : bersih.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ' && c != '.' && c != '\'') {
                return false;
            }
        }
        return true;
    }

    public static boolean passwordValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean adaHuruf = false;
        boolean adaAngka = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                adaHuruf = true;
            } else if (Character.isDigit(c)) {
                adaAngka = true;
            }
        }
        return adaHuruf && adaAngka;
    }

    public static boolean gajiValid(String gaji) {
        if (gaji == null || gaji.isEmpty() || gaji.length() > 10) {
            return false;
        }
        return semuaAngka(gaji);
    }

    public static boolean panjangMaksimal(String teks, int maksimal) {
        return teks != null && teks.length() <= maksimal;
    }

    public static boolean teleponKantorValid(String telepon) {
        if (telepon == null || telepon.length() < 8 || telepon.length() > 15) {
            return false;
        }
        return semuaAngka(telepon);
    }

    public static boolean tanggalLahirValid(LocalDate tanggalLahir) {
        if (tanggalLahir == null || tanggalLahir.isAfter(LocalDate.now())) {
            return false;
        }
        int usia = Period.between(tanggalLahir, LocalDate.now()).getYears();
        return usia >= 15 && usia <= 100;
    }

    public static boolean pdfValid(String namaFile) {
        return namaFile != null && namaFile.toLowerCase().endsWith(".pdf");
    }

    public static boolean ukuranFileValid(long ukuran) {
        return ukuran > 0 && ukuran <= UKURAN_MAKS_FILE;
    }

    public static boolean gambarValid(String namaFile) {
        if (namaFile == null) {
            return false;
        }
        String nama = namaFile.toLowerCase();
        return nama.endsWith(".jpg") || nama.endsWith(".jpeg") || nama.endsWith(".png");
    }

    private static boolean semuaAngka(String teks) {
        for (char c : teks.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}