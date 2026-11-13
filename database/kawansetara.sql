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
    FOREIGN KEY (id_pelamar) REFERENCES Pelamar(id_pelamar),
    FOREIGN KEY (id_lowongan) REFERENCES Lowongan(id_lowongan)
);

CREATE TABLE Keahlian(
    id_keahlian INT AUTO_INCREMENT PRIMARY KEY,
    nama_keahlian VARCHAR(255) NOT NULL
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
('Admin Utama', 'admin@kawansetara.com', 'admin12345');

INSERT INTO Pelamar (nama, email, password, no_telepon, tanggal_lahir, pendidikan) VALUES
('Pelamar Coba', 'pelamar@gmail.com', 'pelamar123', '081234567890', '2004-05-10', 'SMA/SMK');

INSERT INTO Jenis_Disabilitas (nama_disabilitas, deskripsi) VALUES
('Disabilitas Fisik', 'Keterbatasan gerak atau fungsi anggota tubuh'),
('Disabilitas Sensorik Netra', 'Keterbatasan penglihatan'),
('Disabilitas Sensorik Rungu', 'Keterbatasan pendengaran'),
('Disabilitas Wicara', 'Keterbatasan berbicara'),
('Disabilitas Intelektual', 'Keterbatasan fungsi kognitif'),
('Disabilitas Mental', 'Gangguan psikososial atau perkembangan');

INSERT INTO Keahlian (nama_keahlian) VALUES
('Mengetik / Administrasi'),
('Desain Grafis'),
('Pemrograman'),
('Customer Service'),
('Kerajinan Tangan'),
('Akuntansi');

INSERT INTO Perusahaan (nama_perusahaan, alamat, email, no_telepon, deskripsi) VALUES
('PT Contoh Inklusif', 'Jl. Raya Kampus Unud, Jimbaran, Bali', 'hrd@contohinklusif.com', '0361123456', 'Perusahaan contoh untuk data awal');

INSERT INTO Lowongan (id_admin, id_perusahaan, nama_pekerjaan, deskripsi, gaji, lokasi, durasi_kerja, jenis_lowongan, persyaratan) VALUES
(1, 1, 'Staf Administrasi', 'Mengelola data dan dokumen kantor', 3500000.00, 'Denpasar', '8 jam/hari', 'full time', 'Bisa mengoperasikan komputer');

INSERT INTO Lowongan_Keahlian (id_lowongan, id_keahlian) VALUES
(1, 1);

INSERT INTO Lamaran (id_pelamar, id_lowongan, tanggal_lamar) VALUES
(1, 1, CURDATE());

