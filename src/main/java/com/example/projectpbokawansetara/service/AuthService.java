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