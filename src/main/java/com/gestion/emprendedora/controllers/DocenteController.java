package com.gestion.emprendedora.controllers;

import com.gestion.emprendedora.dto.AgendarLlamadaDTO;
import com.gestion.emprendedora.dto.CreateInformeDTO;
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
 * Todas las rutas aquí están protegidas y solo son accesibles para docentes.
 */
@RestController
@RequestMapping("/api/docente")
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    /**
     * Endpoint para OBTENER todas las consultas del sistema.
     * @return Una lista de todas las consultas.
     */
    @GetMapping("/consultas")
    public ResponseEntity<List<Consulta>> getAllConsultas() {
        List<Consulta> consultas = docenteService.getAllConsultas();
        return ResponseEntity.ok(consultas);
    }

    /**
     * Endpoint para que un docente inicie la videollamada para una consulta.
     * @param consultaId El ID de la consulta para la que se inicia la llamada.
     * @return La consulta actualizada con el nombre de la sala de llamada.
     */
    @PostMapping("/consultas/{consultaId}/iniciar-llamada")
    public ResponseEntity<Consulta> iniciarLlamada(@PathVariable Long consultaId) {
        Consulta consultaActualizada = docenteService.iniciarLlamada(consultaId);
        return ResponseEntity.ok(consultaActualizada);
    }

    /**
     * ¡NUEVO ENDPOINT!
     * Endpoint para que un docente agende una videollamada y notifique al estudiante.
     * @param consultaId El ID de la consulta a agendar.
     * @param agendarLlamadaDTO DTO con la fecha y hora seleccionadas.
     * @param principal Objeto de seguridad para identificar al docente.
     * @return La consulta actualizada con el estado "Agendada".
     */
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
     * Endpoint para que un docente cree un informe para una consulta específica.
     * Al crear el informe, también se actualiza el estado de la consulta a "Respondida".
     * @param consultaId El ID de la consulta sobre la que se está informando.
     * @param informeDTO Los datos del informe que vienen en el cuerpo de la petición.
     * @param principal Objeto de seguridad que contiene la información del docente autenticado.
     * @return El informe de sesión recién creado.
     */
    @PostMapping("/consultas/{consultaId}/informe")
    public ResponseEntity<InformeSesion> createInforme(
            @PathVariable Long consultaId,
            @Valid @RequestBody CreateInformeDTO informeDTO,
            Principal principal) {
        
        InformeSesion informeCreado = docenteService.createInformeAndUpdateConsulta(informeDTO, consultaId, principal.getName());
        return ResponseEntity.ok(informeCreado);
    }
}