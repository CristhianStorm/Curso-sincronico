package com.gestion.emprendedora.services;

import com.gestion.emprendedora.dto.CreateInformeDTO;
import com.gestion.emprendedora.entities.Consulta;
import com.gestion.emprendedora.entities.InformeSesion;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.ConsultaRepository;
import com.gestion.emprendedora.repositories.InformeSesionRepository;
import com.gestion.emprendedora.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Servicio que maneja la lógica de negocio para las operaciones del Docente.
 */
@Service
public class DocenteService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private InformeSesionRepository informeSesionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene todas las consultas del sistema.
     * En una versión más avanzada, esto podría filtrar por docente asignado.
     * @return Una lista de todas las consultas.
     */
    public List<Consulta> getAllConsultas() {
        return consultaRepository.findAll();
    }

    /**
     * Crea un informe para una consulta y luego actualiza el estado de la consulta a "Respondida".
     * Se ejecuta como una única transacción para garantizar la integridad de los datos.
     * @param informeDTO Los datos del informe (puntos fuertes, etc.).
     * @param consultaId El ID de la consulta a la que pertenece el informe.
     * @param docenteEmail El email del docente que está creando el informe.
     * @return El informe de sesión recién creado.
     */
    @Transactional
    public InformeSesion createInformeAndUpdateConsulta(CreateInformeDTO informeDTO, Long consultaId, String docenteEmail) {
        Usuario docente = usuarioRepository.findByEmail(docenteEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Docente no encontrado"));

        Consulta consulta = consultaRepository.findById(consultaId)
            .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        InformeSesion nuevoInforme = new InformeSesion();
        nuevoInforme.setIdConsulta(consultaId);
        nuevoInforme.setIdDocente(docente.getId());
        nuevoInforme.setPuntosFuertes(informeDTO.getPuntosFuertes());
        nuevoInforme.setAreasAMejorar(informeDTO.getAreasAMejorar());
        nuevoInforme.setRecomendaciones(informeDTO.getRecomendaciones());
        nuevoInforme.setFechaCreacion(new Date());
        
        informeSesionRepository.save(nuevoInforme);

        consulta.setEstado("Respondida");
        consultaRepository.save(consulta);

        return nuevoInforme;
    }

    /**
     * Inicia una llamada para una consulta.
     * Genera un nombre de sala único, lo guarda en la consulta y actualiza su estado.
     * @param consultaId El ID de la consulta.
     * @return La consulta actualizada con el nombre de la sala.
     */
    public Consulta iniciarLlamada(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
            .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        // Generamos un nombre de sala único y difícil de adivinar
        String nombreSala = "TutorIA-" + consultaId + "-" + UUID.randomUUID().toString().substring(0, 8);
        
        consulta.setSalaLlamada(nombreSala);
        consulta.setEstado("Llamada en curso");
        
        return consultaRepository.save(consulta);
    }
}

