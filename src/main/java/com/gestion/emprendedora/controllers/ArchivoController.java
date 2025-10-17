package com.gestion.emprendedora.controllers;

import com.gestion.emprendedora.entities.ArchivoAdjunto;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.ArchivoAdjuntoRepository;
import com.gestion.emprendedora.repositories.UsuarioRepository;
import com.gestion.emprendedora.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/archivos") // <- Cambiamos la ruta base para que sea más genérica
public class ArchivoController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ArchivoAdjuntoRepository archivoAdjuntoRepository; // <- Añadimos este repositorio

    // --- ¡ENDPOINT ACTUALIZADO! ---
    @PostMapping("/upload/{consultaId}")
    public ResponseEntity<?> subirArchivo(
            @PathVariable Long consultaId,
            @RequestParam("file") MultipartFile file,
            Principal principal) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
            ArchivoAdjunto adjunto = fileStorageService.guardarArchivo(file, consultaId, usuario);
            return ResponseEntity.ok(adjunto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo subir el archivo: " + e.getMessage());
        }
    }

    // --- ¡NUEVO ENDPOINT! ---
    @GetMapping("/consulta/{consultaId}")
    public ResponseEntity<List<ArchivoAdjunto>> listarArchivosPorConsulta(@PathVariable Long consultaId) {
        List<ArchivoAdjunto> archivos = archivoAdjuntoRepository.findByIdConsulta(consultaId);
        return ResponseEntity.ok(archivos);
    }

    // --- ¡NUEVO ENDPOINT! ---
    @GetMapping("/download/{nombreGuardado}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable String nombreGuardado) {
        Resource archivo = fileStorageService.cargarComoRecurso(nombreGuardado);
        
        // Buscamos el nombre original en la base de datos para que la descarga sea amigable
        String nombreOriginal = archivoAdjuntoRepository.findByNombreGuardado(nombreGuardado)
                                    .map(ArchivoAdjunto::getNombreOriginal)
                                    .orElse(archivo.getFilename());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreOriginal + "\"")
                .body(archivo);
    }
}