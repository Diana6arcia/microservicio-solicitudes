package com.example.solicitudes.exception;

public class SolicitudProcessingException extends RuntimeException {

    public SolicitudProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}