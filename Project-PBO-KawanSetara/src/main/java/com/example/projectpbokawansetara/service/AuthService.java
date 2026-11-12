package com.example.projectpbokawansetara.service;

import com.example.projectpbokawansetara.exception.ValidationException;

public class AuthService {

    private static final int MIN_PASSWORD = 8;
    private static final String POLA_EMAIL = "^[\\w.+-]+@[\\w-]+\\.[\\w.]+$";

    public void validasiLogin(String email, String password) throws ValidationException {
        if (email == null || email.isBlank()) {
            throw new ValidationException("email", "Email wajib diisi");
        }
        if (!email.trim().matches(POLA_EMAIL)) {
            throw new ValidationException("email", "Format email tidak valid");
        }
        if (password == null || password.isEmpty()) {
            throw new ValidationException("password", "Kata sandi wajib diisi");
        }
        if (password.length() < MIN_PASSWORD) {
            throw new ValidationException("password", "Kata sandi minimal " + MIN_PASSWORD + " karakter");
        }
    }
}