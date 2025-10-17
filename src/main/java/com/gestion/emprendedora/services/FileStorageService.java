package com.gestion.emprendedora.services;

import com.gestion.emprendedora.entities.ArchivoAdjunto;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.ArchivoAdjuntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads");

    @Autowired
    private ArchivoAdjuntoRepository archivoAdjuntoRepository;

    public FileStorageService() {
        try {
            Files.createDirectories(root);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo inicializar la carpeta para subidas de archivos.", e);
        }
    }

    public ArchivoAdjunto guardarArchivo(MultipartFile file, Long idConsulta, Usuario usuario) {
        try {
            String nombreOriginal = file.getOriginalFilename();
            String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
            String nombreGuardado = UUID.randomUUID().toString() + extension;

            Files.copy(file.getInputStream(), this.root.resolve(nombreGuardado));

            ArchivoAdjunto adjunto = new ArchivoAdjunto();
            adjunto.setIdConsulta(idConsulta);
            adjunto.setIdUsuarioQueSubio(usuario.getId());
            adjunto.setNombreOriginal(nombreOriginal);
            adjunto.setNombreGuardado(nombreGuardado);
            adjunto.setTipoMime(file.getContentType());
            
            return archivoAdjuntoRepository.save(adjunto);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
        }
    }

    // --- ¡NUEVO MÉTODO! ---
    public Resource cargarComoRecurso(String nombreArchivo) {
        try {
            Path archivo = root.resolve(nombreArchivo);
            Resource recurso = new UrlResource(archivo.toUri());

            if (recurso.exists() || recurso.isReadable()) {
                return recurso;
            } else {
                throw new RuntimeException("No se pudo leer el archivo: " + nombreArchivo);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}