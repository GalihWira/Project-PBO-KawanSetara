CREATE DATABASE pbo_kawansetara;
USE pbo_kawansetara;

CREATE TABLE Admin(
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin') NOT NULL DEFAULT 'admin'
);

CREATE TABLE Perusahaan(
    id_perusahaan INT AUTO_INCREMENT PRIMARY KEY,
    nama_perusahaan VARCHAR(150) NOT NULL,
    alamat TEXT,
    email VARCHAR(100),
    no_telepon VARCHAR(20),
    deskripsi TEXT
);

CREATE TABLE Lowongan(
    id_lowongan INT AUTO_INCREMENT PRIMARY KEY,
    id_admin INT NOT NULL,
    id_perusahaan INT NOT NULL,
    nama_pekerjaan VARCHAR(150) NOT NULL,
    deskripsi TEXT,
    gaji DECIMAL(12,2),
    lokasi VARCHAR(150),
    durasi_kerja VARCHAR(100),
    jenis_lowongan ENUM('full time', 'part time') NOT NULL,
    persyaratan TEXT,
    status ENUM('tersedia', 'tidak tersedia') NOT NULL DEFAULT 'tersedia',
    FOREIGN KEY (id_admin) REFERENCES Admin(id_admin),
    FOREIGN KEY (id_perusahaan) REFERENCES Perusahaan(id_perusahaan)
);

CREATE TABLE Pelamar(
    id_pelamar INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    no_telepon VARCHAR(20),
    usia INT,
    alamat TEXT,
    tanggal_lahir DATE,
    pendidikan VARCHAR(100),
    ijazah VARCHAR(255),
    surat_ket_disabilitas VARCHAR(255),
    cv VARCHAR(255),
    foto VARCHAR(255),
    role ENUM('pelamar') NOT NULL DEFAULT 'pelamar'
);

CREATE TABLE Jenis_Disabilitas(
    id_disabilitas INT AUTO_INCREMENT PRIMARY KEY,
    nama_disabilitas VARCHAR(255) NOT NULL,
    deskripsi TEXT
);

CREATE TABLE Lamaran(
    id_lamaran INT AUTO_INCREMENT PRIMARY KEY,
    id_pelamar INT NOT NULL,
    id_lowongan INT NOT NULL,
    tanggal_lamar DATE NOT NULL,
    status ENUM('diproses', 'ditinjau', 'diterima', 'ditolak') NOT NULL DEFAULT 'diproses',
    tanggal_diperbarui DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT unik_lamaran UNIQUE (id_pelamar, id_lowongan),
    FOREIGN KEY (id_pelamar) REFERENCES Pelamar(id_pelamar),
    FOREIGN KEY (id_lowongan) REFERENCES Lowongan(id_lowongan)
);

CREATE TABLE KategoriKeahlian(
    id_kategori INT AUTO_INCREMENT PRIMARY KEY,
    nama_kategori VARCHAR(100) NOT NULL
);

CREATE TABLE Keahlian(
    id_keahlian INT AUTO_INCREMENT PRIMARY KEY,
    nama_keahlian VARCHAR(255) NOT NULL,
    id_kategori INT,
    CONSTRAINT fk_keahlian_kategori FOREIGN KEY (id_kategori) REFERENCES KategoriKeahlian(id_kategori)
);

CREATE TABLE Pelamar_Disabilitas(
    id_pelamar INT NOT NULL,
    id_disabilitas INT NOT NULL,
    PRIMARY KEY (id_pelamar, id_disabilitas),
    FOREIGN KEY (id_pelamar) REFERENCES Pelamar(id_pelamar) ON DELETE CASCADE,
    FOREIGN KEY (id_disabilitas) REFERENCES Jenis_Disabilitas(id_disabilitas) ON DELETE CASCADE
);

CREATE TABLE Pelamar_Keahlian(
    id_pelamar INT NOT NULL,
    id_keahlian INT NOT NULL,
    PRIMARY KEY (id_pelamar, id_keahlian),
    FOREIGN KEY (id_pelamar) REFERENCES Pelamar(id_pelamar) ON DELETE CASCADE,
    FOREIGN KEY (id_keahlian) REFERENCES Keahlian(id_keahlian) ON DELETE CASCADE
);

CREATE TABLE Lowongan_Keahlian(
    id_lowongan INT NOT NULL,
    id_keahlian INT NOT NULL,
    PRIMARY KEY (id_lowongan, id_keahlian),
    FOREIGN KEY (id_lowongan) REFERENCES Lowongan(id_lowongan) ON DELETE CASCADE,
    FOREIGN KEY (id_keahlian) REFERENCES Keahlian(id_keahlian) ON DELETE CASCADE
);

INSERT INTO Admin (nama, email, password) VALUES
('Admin Utama', 'admin@kawansetara.com', '$2a$12$QhMWdG7Ds7BttGV6Kf0Mqu7rQmAhvFn652tDMZmuf6Qu1vRklJCyu');

INSERT INTO Perusahaan (nama_perusahaan, alamat, email, no_telepon, deskripsi) VALUES
('PT Contoh Inklusif', 'Jl. Raya Kampus Unud, Jimbaran, Bali', 'hrd@contohinklusif.com', '0361123456', 'Perusahaan contoh untuk data awal'),
('CV Kreatif Setara', 'Jl. Teuku Umar No. 10, Denpasar', 'info@kreatifsetara.com', '0361765432', 'Studio desain yang ramah disabilitas'),
('PT Belum Buka Lowongan', 'Jl. Bypass Ngurah Rai, Badung', NULL, NULL, 'Perusahaan mitra tanpa lowongan, untuk tes statistik dashboard');

INSERT INTO Pelamar (nama, email, password, no_telepon, usia, tanggal_lahir, pendidikan, ijazah, surat_ket_disabilitas, cv) VALUES
('Pelamar Coba', 'pelamar@gmail.com', '$2a$12$/2QDVkHGJ.Qro9j9/te/q..xgd//.BMDg5RFSiikMyrmc.hGzmiua', '081234567890', 22, '2004-05-10', 'SMA', 'uploads/ijazah/contoh_ijazah_pelamar.pdf', 'uploads/skd/contoh_skd_pelamar.pdf', 'uploads/cv/contoh_cv_pelamar.pdf'),
('Budi Santoso', 'budi@gmail.com', '$2a$12$DmY5mEjDtaaqIgmbGrbG5O6doc4Kz2L49iPYHNVkcAepHNLRfKxYy', '081298765432', 24, '2002-08-17', 'Sarjana', 'uploads/ijazah/contoh_ijazah_budi.pdf', 'uploads/skd/contoh_skd_budi.pdf', 'uploads/cv/contoh_cv_budi.pdf'),
('Sari Dewi', 'sari@yahoo.com', '$2a$12$ZqiD/S8CP8rc9J84kOk03OtWvp5zpgIiz/WGFIytpRaMeqASrBQ.2', '085712345678', 23, '2003-01-25', 'Lainnya', 'uploads/ijazah/contoh_ijazah_sari.pdf', 'uploads/skd/contoh_skd_sari.pdf', 'uploads/cv/contoh_cv_sari.pdf');

INSERT INTO Jenis_Disabilitas (nama_disabilitas, deskripsi) VALUES
('Disabilitas Fisik', 'Keterbatasan gerak atau fungsi anggota tubuh'),
('Disabilitas Sensorik Netra', 'Keterbatasan penglihatan'),
('Disabilitas Sensorik Rungu', 'Keterbatasan pendengaran'),
('Disabilitas Wicara', 'Keterbatasan berbicara'),
('Disabilitas Intelektual', 'Keterbatasan fungsi kognitif'),
('Disabilitas Mental', 'Gangguan psikososial atau perkembangan');

INSERT INTO KategoriKeahlian (nama_kategori) VALUES
('Administrasi'),
('Desain'),
('Teknologi'),
('Komunikasi'),
('Kreativitas'),
('Keuangan');

INSERT INTO Keahlian (nama_keahlian, id_kategori) VALUES
('Mengetik / Administrasi', 1),
('Desain Grafis', 2),
('Pemrograman', 3),
('Customer Service', 4),
('Kerajinan Tangan', 5),
('Akuntansi', 6);

INSERT INTO Lowongan (id_admin, id_perusahaan, nama_pekerjaan, deskripsi, gaji, lokasi, durasi_kerja, jenis_lowongan, persyaratan, status) VALUES
(1, 1, 'Staf Administrasi', 'Mengelola data dan dokumen kantor', 3500000, 'Denpasar', '8 jam/hari', 'full time', 'Bisa mengoperasikan komputer', 'tersedia'),
(1, 2, 'Desainer Grafis', 'Membuat desain konten', 4000000, 'Badung', '6 jam/hari', 'full time', 'Bisa Canva', 'tersedia'),
(1, 1, 'Penulis Konten', 'Menulis artikel', 2500000, 'Gianyar', '4 jam/hari', 'part time', 'Bisa menulis', 'tidak tersedia');

INSERT INTO Lowongan_Keahlian (id_lowongan, id_keahlian) VALUES
(1, 1),
(1, 4),
(2, 2),
(2, 5),
(3, 4);

INSERT INTO Pelamar_Disabilitas (id_pelamar, id_disabilitas) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 3),
(3, 4);

INSERT INTO Pelamar_Keahlian (id_pelamar, id_keahlian) VALUES
(1, 1),
(1, 4),
(2, 2),
(3, 1),
(3, 6);

INSERT INTO Lamaran (id_pelamar, id_lowongan, tanggal_lamar, status) VALUES
(1, 1, CURDATE(), 'diproses'),
(2, 2, CURDATE() - INTERVAL 2 DAY, 'ditinjau'),
(3, 1, CURDATE() - INTERVAL 5 DAY, 'diterima'),
(1, 2, CURDATE() - INTERVAL 7 DAY, 'ditolak');