package com.example.solicitudes.dto;

public class EvidenciaResponse {

    private String archivo;
    private boolean existe;

    public EvidenciaResponse(String archivo, boolean existe) {
        this.archivo = archivo;
        this.existe = existe;
    }

    public String getArchivo() {
        return archivo;
    }

    public boolean isExiste() {
        return existe;
    }
}