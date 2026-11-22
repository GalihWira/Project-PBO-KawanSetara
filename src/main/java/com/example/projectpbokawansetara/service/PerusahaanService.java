package com.example.projectpbokawansetara.service;

import com.example.projectpbokawansetara.exception.ValidationException;
import com.example.projectpbokawansetara.helper.ValidasiHelper;

public class PerusahaanService {

    private static final int MAKS_NAMA = 150;
    private static final int MAKS_EMAIL = 100;

    public void validasiPerusahaan(String nama, String email, String telepon) throws ValidationException {
        validasiNama(nama);
        validasiEmail(email);
        validasiTelepon(telepon);
    }

    private void validasiNama(String nama) throws ValidationException {
        if (nama == null || nama.isEmpty()) {
            throw new ValidationException("nama", "Nama perusahaan wajib diisi!");
        }
        if (!ValidasiHelper.panjangMaksimal(nama, MAKS_NAMA)) {
            throw new ValidationException("nama", "Nama perusahaan maksimal 150 karakter!");
        }
    }

    private void validasiEmail(String email) throws ValidationException {
        if (email == null || email.isEmpty()) {
            return;
        }
        if (!ValidasiHelper.panjangMaksimal(email, MAKS_EMAIL)) {
            throw new ValidationException("email", "Email maksimal 100 karakter!");
        }
        if (!ValidasiHelper.emailValid(email)) {
            throw new ValidationException("email", "Email tidak valid!");
        }
    }

    private void validasiTelepon(String telepon) throws ValidationException {
        if (telepon == null || telepon.isEmpty()) {
            return;
        }
        if (telepon.matches(".*[a-zA-Z].*")) {
            throw new ValidationException("telepon", "Nomor telepon tidak boleh mengandung huruf!");
        }
    }
}