package com.example.solicitudes.dto;

public class ErrorResponse {

    private String error;
    private Object detail;

    public ErrorResponse(String error, Object detail) {
        this.error = error;
        this.detail = detail;
    }

    public String getError() {
        return error;
    }

    public Object getDetail() {
        return detail;
    }
}