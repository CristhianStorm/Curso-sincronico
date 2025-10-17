package com.gestion.emprendedora.controllers;

import com.gestion.emprendedora.dto.AgendarLlamadaDTO;
import com.gestion.emprendedora.dto.CreateInformeDTO;
import com.gestion.emprendedora.dto.RespuestaTextoDTO;
import com.gestion.emprendedora.entities.Consulta;
import com.gestion.emprendedora.entities.InformeSesion;
import com.gestion.emprendedora.services.DocenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controlador REST para manejar las peticiones de un usuario con rol 'DOCENTE'.
 */
@RestController
@RequestMapping("/api/docente")
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @GetMapping("/consultas")
    public ResponseEntity<List<Consulta>> getAllConsultas() {
        List<Consulta> consultas = docenteService.getAllConsultas();
        return ResponseEntity.ok(consultas);
    }

    @PostMapping("/consultas/{consultaId}/iniciar-llamada")
    public ResponseEntity<Consulta> iniciarLlamada(@PathVariable Long consultaId) {
        Consulta consultaActualizada = docenteService.iniciarLlamada(consultaId);
        return ResponseEntity.ok(consultaActualizada);
    }

    @PostMapping("/consultas/{consultaId}/agendar")
    public ResponseEntity<Consulta> agendarLlamada(
            @PathVariable Long consultaId,
            @Valid @RequestBody AgendarLlamadaDTO agendarLlamadaDTO,
            Principal principal) {

        Consulta consultaActualizada = docenteService.agendarLlamada(
            consultaId,
            agendarLlamadaDTO.getFechaHora(),
            principal.getName()
        );
        return ResponseEntity.ok(consultaActualizada);
    }

    /**
     * ¡NUEVO ENDPOINT!
     * Endpoint para que un docente envíe una respuesta de texto rápida.
     */
    @PostMapping("/consultas/{consultaId}/responder")
    public ResponseEntity<Consulta> responderConsulta(
            @PathVariable Long consultaId,
            @Valid @RequestBody RespuestaTextoDTO respuestaDTO,
            Principal principal) {
        
        Consulta consultaActualizada = docenteService.responderConsultaTexto(
            consultaId, 
            respuestaDTO.getRespuesta(), 
            principal.getName()
        );
        return ResponseEntity.ok(consultaActualizada);
    }

    @PostMapping("/consultas/{consultaId}/informe")
    public ResponseEntity<InformeSesion> createInforme(
            @PathVariable Long consultaId,
            @Valid @RequestBody CreateInformeDTO informeDTO,
            Principal principal) {
        
        InformeSesion informeCreado = docenteService.createInformeAndUpdateConsulta(informeDTO, consultaId, principal.getName());
        return ResponseEntity.ok(informeCreado);
    }
}