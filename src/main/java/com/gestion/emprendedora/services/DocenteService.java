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

    @Autowired
    private EmailService emailService;

    /**
     * Obtiene todas las consultas del sistema.
     * @return Una lista de todas las consultas.
     */
    public List<Consulta> getAllConsultas() {
        return consultaRepository.findAll();
    }

    /**
     * ¡MÉTODO CORREGIDO!
     * Orquesta el agendamiento de una llamada, actualizando la consulta y notificando por email.
     * @param consultaId El ID de la consulta a agendar.
     * @param fechaHoraTimestamp El timestamp en milisegundos de la fecha y hora para la llamada.
     * @param docenteEmail El email del docente que realiza la acción.
     * @return La consulta actualizada.
     */
    @Transactional
    public Consulta agendarLlamada(Long consultaId, Long fechaHoraTimestamp, String docenteEmail) {
        Usuario docente = usuarioRepository.findByEmail(docenteEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Docente no encontrado"));

        Consulta consulta = consultaRepository.findById(consultaId)
            .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        Usuario estudiante = usuarioRepository.findById(consulta.getIdEstudiante())
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado para la consulta"));

        // Convertimos el timestamp (Long) a un objeto Date de Java
        Date fechaAgendada = new Date(fechaHoraTimestamp);

        // Actualizamos la consulta
        consulta.setFechaLlamadaAgendada(fechaAgendada);
        consulta.setEstado("Agendada");
        consulta.setIdDocenteAsignado(docente.getId());
        Consulta consultaActualizada = consultaRepository.save(consulta);

        // Enviamos el correo de notificación
        emailService.sendSchedulingConfirmationEmail(
            estudiante.getEmail(),
            estudiante.getEmailTutor(), // Puede ser null, el EmailService lo maneja
            estudiante.getNombreCompleto(),
            docente.getNombreCompleto(),
            fechaAgendada,
            consulta.getTitulo()
        );

        return consultaActualizada;
    }


    /**
     * Crea un informe para una consulta y luego actualiza el estado de la consulta a "Respondida".
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
     * @param consultaId El ID de la consulta.
     * @return La consulta actualizada con el nombre de la sala.
     */
    public Consulta iniciarLlamada(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
            .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        String nombreSala = "TutorIA-" + consultaId + "-" + UUID.randomUUID().toString().substring(0, 8);
        
        consulta.setSalaLlamada(nombreSala);
        consulta.setEstado("Llamada en curso");
        
        return consultaRepository.save(consulta);
    }
}