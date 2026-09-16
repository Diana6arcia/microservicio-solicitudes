package com.example.solicitudes.exception;

public class SolicitudNotFoundException extends RuntimeException {

    public SolicitudNotFoundException(String message) {
        super(message);
    }
}