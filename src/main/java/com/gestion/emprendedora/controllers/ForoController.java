package com.gestion.emprendedora.controllers;

import com.gestion.emprendedora.dto.CreateHiloDTO;
import com.gestion.emprendedora.dto.CreateRespuestaDTO;
import com.gestion.emprendedora.dto.HiloConRespuestasDTO;
import com.gestion.emprendedora.entities.ForoHilo;
import com.gestion.emprendedora.entities.ForoRespuesta;
import com.gestion.emprendedora.services.ForoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controlador REST para manejar las peticiones del Foro Comunitario.
 */
@RestController
@RequestMapping("/api/foro")
public class ForoController {

    @Autowired
    private ForoService foroService;

    /**
     * Endpoint PÚBLICO para OBTENER una lista de todos los hilos de discusión.
     * @return Una lista de todos los hilos del foro.
     */
    @GetMapping("/hilos")
    public ResponseEntity<List<ForoHilo>> getAllHilos() {
        return ResponseEntity.ok(foroService.getAllHilos());
    }

    /**
     * Endpoint PÚBLICO para OBTENER los detalles de un hilo específico, incluyendo sus respuestas.
     * @param hiloId El ID del hilo a buscar.
     * @return Un DTO que contiene el hilo, el nombre del autor y una lista de respuestas con sus autores.
     */
    @GetMapping("/hilos/{hiloId}")
    public ResponseEntity<HiloConRespuestasDTO> getHiloById(@PathVariable Long hiloId) {
        return ResponseEntity.ok(foroService.getHiloConRespuestas(hiloId));
    }

    /**
     * Endpoint PROTEGIDO para que un usuario autenticado cree un nuevo hilo.
     * @param hiloDTO Los datos del nuevo hilo (título, contenido, categoría).
     * @param principal Objeto de seguridad que contiene el email del usuario logueado.
     * @return El hilo recién creado.
     */
    @PostMapping("/hilos")
    public ResponseEntity<ForoHilo> createHilo(@Valid @RequestBody CreateHiloDTO hiloDTO, Principal principal) {
        ForoHilo nuevoHilo = foroService.createHilo(hiloDTO, principal.getName());
        return ResponseEntity.ok(nuevoHilo);
    }

    /**
     * Endpoint PROTEGIDO para que un usuario autenticado publique una respuesta en un hilo.
     * @param hiloId El ID del hilo donde se publicará la respuesta.
     * @param respuestaDTO El contenido de la respuesta.
     * @param principal Objeto de seguridad que contiene el email del usuario logueado.
     * @return La respuesta recién creada.
     */
    @PostMapping("/hilos/{hiloId}/respuestas")
    public ResponseEntity<ForoRespuesta> createRespuesta(
            @PathVariable Long hiloId,
            @Valid @RequestBody CreateRespuestaDTO respuestaDTO,
            Principal principal) {
        
        ForoRespuesta nuevaRespuesta = foroService.createRespuesta(respuestaDTO, hiloId, principal.getName());
        return ResponseEntity.ok(nuevaRespuesta);
    }
}

