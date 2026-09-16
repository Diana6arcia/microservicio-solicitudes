package com.example.solicitudes.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.InputStream;

@Service
public class SftpService {

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.remote-path}")
    private String remotePath;

    public void conectar() throws Exception {

        JSch jsch = new JSch();

        Session session = jsch.getSession(
                username,
                host,
                port
        );

        session.setPassword(password);

        session.setConfig(
                "StrictHostKeyChecking",
                "no"
        );

        session.connect();

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");

        channel.connect();

        System.out.println("Conexión SFTP exitosa");

        channel.disconnect();
        session.disconnect();
    }

    public void subirArchivo(String archivoLocal) throws Exception {

        Path archivo = Path.of(archivoLocal).toAbsolutePath();

        JSch jsch = new JSch();

        Session session = jsch.getSession(
                username,
                host,
                port
        );

        session.setPassword(password);

        session.setConfig(
                "StrictHostKeyChecking",
                "no"
        );

        session.connect();

        ChannelSftp channel =
                (ChannelSftp) session.openChannel("sftp");

        channel.connect();

        channel.cd(remotePath);

        String nombreArchivo = archivo.getFileName().toString();

        try (InputStream inputStream = Files.newInputStream(archivo)) {

            channel.put(
                    inputStream,
                    nombreArchivo
            );
        }

        channel.disconnect();
        session.disconnect();
    }
}