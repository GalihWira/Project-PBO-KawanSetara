package com.example.projectpbokawansetara.service;

import com.example.projectpbokawansetara.exception.ValidationException;
import com.example.projectpbokawansetara.helper.ValidasiHelper;

public class AuthService {

    public void validasiLogin(String email, String password) throws ValidationException {
        validasiFormatEmail(email);

        if (password == null || password.isEmpty()) {
            throw new ValidationException("password", "Kata sandi wajib diisi!");
        }
    }

    public void validasiAkunDaftar(String nama, String email, String password, String konfirmasi)
            throws ValidationException {
        if (nama == null || nama.trim().isEmpty()) {
            throw new ValidationException("nama", "Nama wajib diisi!");
        }
        if (!ValidasiHelper.namaValid(nama)) {
            throw new ValidationException("nama", "Nama hanya boleh huruf, 3 sampai 50 karakter!");
        }

        validasiEmailDaftar(email);

        if (password == null || password.isEmpty()) {
            throw new ValidationException("password", "Kata sandi wajib diisi!");
        }
        if (!ValidasiHelper.passwordValid(password)) {
            throw new ValidationException("password", "Kata sandi minimal 8 karakter, berisi huruf dan angka!");
        }

        if (konfirmasi == null || konfirmasi.isEmpty()) {
            throw new ValidationException("konfirmasi", "Konfirmasi kata sandi wajib diisi!");
        }
        if (!konfirmasi.equals(password)) {
            throw new ValidationException("konfirmasi", "Kata sandi tidak sama!");
        }
    }

    public void validasiTelepon(String telepon) throws ValidationException {
        if (telepon == null || telepon.isEmpty()) {
            throw new ValidationException("telepon", "Nomor telepon wajib diisi!");
        }
        if (!ValidasiHelper.teleponValid(telepon)) {
            throw new ValidationException("telepon", "Nomor telepon diawali 08, 10 sampai 13 angka!");
        }
    }

    public void validasiEmailDaftar(String email) throws ValidationException {
        validasiFormatEmail(email);

        if (!ValidasiHelper.domainDiizinkan(email)) {
            throw new ValidationException("email", "Email tidak valid!");
        }
    }

    private void validasiFormatEmail(String email) throws ValidationException {
        if (email == null || email.isEmpty()) {
            throw new ValidationException("email", "Email wajib diisi!");
        }

        if (!ValidasiHelper.emailValid(email)) {
            throw new ValidationException("email", "Format email tidak valid!");
        }
    }
}