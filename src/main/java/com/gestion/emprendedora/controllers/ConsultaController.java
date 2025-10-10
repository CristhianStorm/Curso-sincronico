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

import java.security.Principal;
import java.util.List;

/**
 * Controlador REST para manejar las operaciones de un estudiante sobre sus propias consultas.
 * Todas las rutas aquí están protegidas y requieren que el usuario esté autenticado.
 */
@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    // ¡NUEVO! Inyectamos el servicio de informes.
    @Autowired
    private InformeService informeService;

    /**
     * Endpoint para OBTENER las consultas del usuario que ha iniciado sesión.
     * Se accede a través de GET /api/consultas.
     * @param principal Objeto de seguridad que contiene el email del usuario logueado.
     * @return Una lista de las consultas del estudiante.
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
     * Endpoint para CREAR una nueva consulta para el usuario logueado.
     * Se accede a través de POST /api/consultas.
     * @param consultaDTO Los datos de la nueva consulta.
     * @param principal El usuario autenticado.
     * @return La consulta recién creada.
     */
    @PostMapping
    public ResponseEntity<Consulta> createConsulta(@Valid @RequestBody CreateConsultaDTO consultaDTO, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        Consulta nuevaConsulta = consultaService.createConsulta(consultaDTO, principal.getName());
        return ResponseEntity.ok(nuevaConsulta);
    }

    /**
     * ¡NUEVO ENDPOINT!
     * Endpoint para que un estudiante obtenga el informe de una de sus consultas.
     * Se accede a través de GET /api/consultas/{consultaId}/informe
     * @param consultaId El ID de la consulta de la que se quiere el informe.
     * @return El informe de sesión si existe.
     */
    @GetMapping("/{consultaId}/informe")
    public ResponseEntity<InformeSesion> getInformeForConsulta(@PathVariable Long consultaId) {
        try {
            InformeSesion informe = informeService.getInformeByConsultaId(consultaId);
            return ResponseEntity.ok(informe);
        } catch (RuntimeException e) {
            // Si no se encuentra un informe, devolvemos un 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
}

