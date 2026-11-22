package com.example.projectpbokawansetara.service;

import com.example.projectpbokawansetara.exception.ValidationException;
import com.example.projectpbokawansetara.helper.ValidasiHelper;

public class PerusahaanService {

    private static final int MAKS_NAMA = 150;
    private static final int MAKS_EMAIL = 100;

    public void validasiPerusahaan(String nama, String alamat, String email, String telepon, String deskripsi)
            throws ValidationException {
        validasiNama(nama);
        validasiAlamat(alamat);
        validasiEmail(email);
        validasiTelepon(telepon);
        validasiDeskripsi(deskripsi);
    }

    private void validasiNama(String nama) throws ValidationException {
        if (nama == null || nama.isEmpty()) {
            throw new ValidationException("nama", "Nama perusahaan wajib diisi!");
        }
        if (!ValidasiHelper.panjangMaksimal(nama, MAKS_NAMA)) {
            throw new ValidationException("nama", "Nama perusahaan maksimal 150 karakter!");
        }
    }

    private void validasiAlamat(String alamat) throws ValidationException {
        if (alamat == null || alamat.isEmpty()) {
            throw new ValidationException("alamat", "Alamat wajib diisi!");
        }
    }

    private void validasiEmail(String email) throws ValidationException {
        if (email == null || email.isEmpty()) {
            throw new ValidationException("email", "Email wajib diisi!");
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
            throw new ValidationException("telepon", "Nomor telepon wajib diisi!");
        }
        if (!ValidasiHelper.teleponKantorValid(telepon)) {
            throw new ValidationException("telepon", "Nomor telepon harus 8 sampai 15 angka!");
        }
    }

    private void validasiDeskripsi(String deskripsi) throws ValidationException {
        if (deskripsi == null || deskripsi.isEmpty()) {
            throw new ValidationException("deskripsi", "Deskripsi wajib diisi!");
        }
    }
}