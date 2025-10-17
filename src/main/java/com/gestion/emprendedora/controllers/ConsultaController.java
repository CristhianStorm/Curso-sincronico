package com.gestion.emprendedora.controllers;

import com.gestion.emprendedora.dto.CreateConsultaDTO;
import com.gestion.emprendedora.entities.Consulta;
import com.gestion.emprendedora.entities.InformeSesion;
import com.gestion.emprendedora.services.ConsultaService;
import com.gestion.emprendedora.services.InformeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

/**
 * Controlador REST para manejar las operaciones de un estudiante sobre sus propias consultas.
 */
@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private InformeService informeService;

    /**
     * Endpoint para OBTENER las consultas del usuario que ha iniciado sesión.
     */
    @GetMapping
    public ResponseEntity<List<Consulta>> getMisConsultas(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        List<Consulta> consultas = consultaService.findConsultasByEmail(principal.getName());
        return ResponseEntity.ok(consultas);
    }

    /**
     * ¡MÉTODO CORREGIDO!
     * Endpoint para CREAR una nueva consulta, aceptando un DTO JSON y un archivo opcional.
     * La anotación @PostMapping sin 'consumes' permite a Spring Boot manejar el multipart/form-data automáticamente.
     */
    @PostMapping
    public ResponseEntity<Consulta> createConsulta(
            @RequestPart("consulta") @Valid CreateConsultaDTO consultaDTO,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Principal principal) {
        
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        Consulta nuevaConsulta = consultaService.createConsulta(consultaDTO, principal.getName(), file);
        return ResponseEntity.ok(nuevaConsulta);
    }

    /**
     * Endpoint para que un estudiante obtenga el informe de una de sus consultas.
     */
    @GetMapping("/{consultaId}/informe")
    public ResponseEntity<InformeSesion> getInformeForConsulta(@PathVariable Long consultaId) {
        try {
            InformeSesion informe = informeService.getInformeByConsultaId(consultaId);
            return ResponseEntity.ok(informe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}