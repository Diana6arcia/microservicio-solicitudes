package com.example.solicitudes.exception;

public class InvalidSolicitudIdException extends RuntimeException {

    public InvalidSolicitudIdException(String message) {
        super(message);
    }
}