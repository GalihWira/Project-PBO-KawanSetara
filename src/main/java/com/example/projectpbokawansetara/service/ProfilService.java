package com.example.projectpbokawansetara.service;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import com.example.projectpbokawansetara.exception.ValidationException;
import com.example.projectpbokawansetara.helper.ValidasiHelper;

public class ProfilService {

    public void validasiProfil(String nama, String telepon, LocalDate tanggalLahir, String pendidikan,
            List<Integer> daftarIdDisabilitas) throws ValidationException {
        if (nama == null || nama.trim().isEmpty()) {
            throw new ValidationException("nama", "Nama wajib diisi!");
        }
        if (!ValidasiHelper.namaValid(nama)) {
            throw new ValidationException("nama", "Nama hanya boleh huruf, 3 sampai 50 karakter!");
        }

        if (telepon == null || telepon.isEmpty()) {
            throw new ValidationException("telepon", "Nomor telepon wajib diisi!");
        }
        if (!ValidasiHelper.teleponValid(telepon)) {
            throw new ValidationException("telepon", "Nomor telepon tidak valid!");
        }

        if (tanggalLahir == null) {
            throw new ValidationException("tanggalLahir", "Tanggal lahir wajib diisi!");
        }
        if (!ValidasiHelper.tanggalLahirValid(tanggalLahir)) {
            throw new ValidationException("tanggalLahir", "Tanggal lahir tidak valid!");
        }

        if (pendidikan == null || pendidikan.isEmpty()) {
            throw new ValidationException("pendidikan", "Pendidikan wajib dipilih!");
        }

        if (daftarIdDisabilitas == null || daftarIdDisabilitas.isEmpty()) {
            throw new ValidationException("disabilitas", "Pilih minimal satu jenis disabilitas!");
        }
    }

    public void validasiDokumen(String cv, String ijazah, String suratKeterangan) throws ValidationException {
        if (cv == null || cv.isEmpty()) {
            throw new ValidationException("dokumen", "CV wajib diunggah!");
        }
        if (ijazah == null || ijazah.isEmpty()) {
            throw new ValidationException("dokumen", "Ijazah wajib diunggah!");
        }
        if (suratKeterangan == null || suratKeterangan.isEmpty()) {
            throw new ValidationException("dokumen", "Surat keterangan disabilitas wajib diunggah!");
        }
    }

    public void validasiFilePdf(File file) throws ValidationException {
        if (!ValidasiHelper.pdfValid(file.getName())) {
            throw new ValidationException("dokumen", "File harus berformat PDF!");
        }
        if (!ValidasiHelper.ukuranFileValid(file.length())) {
            throw new ValidationException("dokumen", "Ukuran file maksimal 2 MB!");
        }
    }

    public void validasiFoto(File file) throws ValidationException {
        if (!ValidasiHelper.gambarValid(file.getName())) {
            throw new ValidationException("foto", "Foto harus berformat JPG atau PNG!");
        }
        if (!ValidasiHelper.ukuranFileValid(file.length())) {
            throw new ValidationException("foto", "Ukuran foto maksimal 2 MB!");
        }
    }
}