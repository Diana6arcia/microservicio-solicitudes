package com.example.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SolicitudRequest {

    @NotBlank(message = "clienteId es obligatorio")
    private String clienteId;

    @NotNull(message = "tipo es obligatorio")
    @Pattern(
            regexp = "ALTA|CAMBIO|BAJA",
            message = "tipo debe ser ALTA, CAMBIO o BAJA"
    )
    private String tipo;

    @NotBlank(message = "descripcion es obligatoria")
    @Size(max = 500, message = "descripcion no puede superar 500 caracteres")
    private String descripcion;

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}