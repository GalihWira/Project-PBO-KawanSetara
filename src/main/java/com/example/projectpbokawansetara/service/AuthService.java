package com.example.projectpbokawansetara.service;

import com.example.projectpbokawansetara.exception.ValidationException;
import com.example.projectpbokawansetara.helper.ValidasiHelper;

public class AuthService {

    public void validasiLogin(String email, String password) throws ValidationException {
        if (email == null || email.isBlank()) {
            throw new ValidationException("email", "Email wajib diisi!");
        }
        if (!ValidasiHelper.emailValid(email.trim())) {
            throw new ValidationException("email", "Format email tidak sesuai!");
        }
        if (password == null || password.isEmpty()) {
            throw new ValidationException("password", "Kata sandi wajib diisi!");
        }
    }
}