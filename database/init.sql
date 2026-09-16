CREATE DATABASE IF NOT EXISTS solicitudes_db;

USE solicitudes_db;

CREATE TABLE IF NOT EXISTS solicitudes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    descripcion TEXT NOT NULL,
    estado VARCHAR(30) NOT NULL,
    fecha_registro DATETIME NOT NULL,
    correlation_id VARCHAR(100) NOT NULL
);

CREATE INDEX idx_solicitudes_correlation_id
ON solicitudes(correlation_id);
