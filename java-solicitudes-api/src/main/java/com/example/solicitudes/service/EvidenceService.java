package com.example.solicitudes.service;

import com.example.solicitudes.dto.SolicitudResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.solicitudes.dto.EvidenciaResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class EvidenceService {

    private final ObjectMapper objectMapper;

    @Value("${evidence.path}")
    private String evidencePath;

    public EvidenceService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String guardarEvidencia(SolicitudResponse solicitud) throws IOException {

        Path directorio = Path.of(evidencePath);
        System.out.println("evidence.path = " + directorio);
        Files.createDirectories(directorio);

        String nombreArchivo = "solicitud-" + solicitud.getId() + ".json";

        Path archivo = directorio.resolve(nombreArchivo);

        String json = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(solicitud);

        Files.writeString(archivo, json);

        return nombreArchivo;
    }

    public EvidenciaResponse consultarEvidencia(Long id) {

        String nombreArchivo = "solicitud-" + id + ".json";

        Path archivo = Path.of(evidencePath, nombreArchivo);

        boolean existe = Files.exists(archivo);

        return new EvidenciaResponse(
                nombreArchivo,
                existe
        );
    }
}