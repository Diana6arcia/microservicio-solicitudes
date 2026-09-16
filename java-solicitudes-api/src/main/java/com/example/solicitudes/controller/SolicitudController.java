package com.example.solicitudes.controller;

import com.example.solicitudes.dto.SolicitudRequest;
import com.example.solicitudes.dto.SolicitudResponse;
import com.example.solicitudes.service.SolicitudService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.solicitudes.exception.InvalidSolicitudIdException;
import com.example.solicitudes.dto.SolicitudConsultaResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<SolicitudResponse> crearSolicitud(
            @RequestBody @Valid SolicitudRequest solicitudRequest) {

        SolicitudResponse response =
                solicitudService.crearSolicitud(solicitudRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudConsultaResponse> consultarSolicitud(
            @PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new InvalidSolicitudIdException(
                    "El id debe ser un número entero mayor que cero"
            );
        }

        SolicitudConsultaResponse response =
                solicitudService.consultarSolicitud(id);

        return ResponseEntity.ok(response);
    }

}