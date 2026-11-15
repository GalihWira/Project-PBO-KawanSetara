package com.example.projectpbokawansetara.helper;

public class ValidasiHelper {

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
        if (nama == null || nama.length() < 3 || nama.length() > 50) {
            return false;
        }
        for (char c : nama.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
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

    public static boolean semuaAngka(String teks) {
        if (teks == null || teks.isEmpty()) {
            return false;
        }
        for (char c : teks.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}