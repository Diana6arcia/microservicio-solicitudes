package com.example.solicitudes.service;

import com.example.solicitudes.dto.SolicitudRequest;
import com.example.solicitudes.dto.SolicitudResponse;
import com.example.solicitudes.entity.Solicitud;
import com.example.solicitudes.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import com.example.solicitudes.service.EvidenceService;
import com.example.solicitudes.service.SftpService;
import com.example.solicitudes.exception.SolicitudNotFoundException;
import com.example.solicitudes.dto.EvidenciaResponse;
import com.example.solicitudes.dto.SolicitudConsultaResponse;
import com.example.solicitudes.exception.SolicitudProcessingException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final EvidenceService evidenceService;
    private final SftpService sftpService;

    @Value("${evidence.path}")
    private String evidencePath;

    public SolicitudService(
            SolicitudRepository solicitudRepository,
            EvidenceService evidenceService,
            SftpService sftpService) {

        this.solicitudRepository = solicitudRepository;
        this.evidenceService = evidenceService;
        this.sftpService = sftpService;
    }

    public SolicitudResponse crearSolicitud(SolicitudRequest solicitudRequest) {

        Solicitud solicitud = new Solicitud();

        solicitud.setClienteId(solicitudRequest.getClienteId());
        solicitud.setTipo(solicitudRequest.getTipo());
        solicitud.setDescripcion(solicitudRequest.getDescripcion());

        solicitud.setEstado("REGISTRADA");
        solicitud.setFechaRegistro(LocalDateTime.now());
        solicitud.setCorrelationId(UUID.randomUUID().toString());

        //Guarda en MySQL
        Solicitud solicitudGuardada;
        try {

            solicitudGuardada = solicitudRepository.save(solicitud);

        } catch (Exception e) {

            throw new SolicitudProcessingException(
                    "No fue posible guardar la solicitud en base de datos.",
                    e
            );
        }

        //Construye respuesta
        SolicitudResponse response = new SolicitudResponse();

        response.setId(solicitudGuardada.getId());
        response.setClienteId(solicitudGuardada.getClienteId());
        response.setTipo(solicitudGuardada.getTipo());
        response.setDescripcion(solicitudGuardada.getDescripcion());
        response.setEstado(solicitudGuardada.getEstado());
        response.setFechaRegistro(solicitudGuardada.getFechaRegistro());
        response.setCorrelationId(solicitudGuardada.getCorrelationId());

        // Generar archivo JSON
        try {

            String nombreArchivo =
                    evidenceService.guardarEvidencia(response);

            String archivoLocal = evidencePath + "/" + nombreArchivo;

            sftpService.subirArchivo(archivoLocal);

        } catch (IOException e) {

            throw new SolicitudProcessingException(
                    "No fue posible generar el archivo de evidencia.",
                    e
            );

        } catch (Exception e) {

            throw new SolicitudProcessingException(
                    "No fue posible enviar la evidencia al servidor SFTP.",
                    e
            );
        }

        return response;
    }

    public SolicitudConsultaResponse consultarSolicitud(Long id) {

        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() ->
                        new SolicitudNotFoundException(
                                "La solicitud con id " + id + " no existe"
                        )
                );

        SolicitudConsultaResponse response =
                new SolicitudConsultaResponse();

        response.setId(solicitud.getId());
        response.setClienteId(solicitud.getClienteId());
        response.setTipo(solicitud.getTipo());
        response.setDescripcion(solicitud.getDescripcion());
        response.setEstado(solicitud.getEstado());
        response.setFechaRegistro(solicitud.getFechaRegistro());
        response.setCorrelationId(solicitud.getCorrelationId());

        EvidenciaResponse evidencia =
                evidenceService.consultarEvidencia(id);

        response.setEvidencia(evidencia);

        return response;
    }
}