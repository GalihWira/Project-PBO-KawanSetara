package com.example.projectpbokawansetara.exception;

public class ValidationException extends Exception {
    private final String namaField;

    public ValidationException(String namaField, String pesan) {
        super(pesan);
        this.namaField = namaField;
    }

    public ValidationException(String pesan) {
        this(null, pesan);
    }

    public String getNamaField() {
        return namaField;
    }
}
