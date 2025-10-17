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

    @Autowired
    private PdfService pdfService;

    public List<Consulta> getAllConsultas() {
        return consultaRepository.findAll();
    }

    @Transactional
    public Consulta agendarLlamada(Long consultaId, Long fechaHoraTimestamp, String docenteEmail) {
        Usuario docente = usuarioRepository.findByEmail(docenteEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Docente no encontrado"));

        Consulta consulta = consultaRepository.findById(consultaId)
            .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        Usuario estudiante = usuarioRepository.findById(consulta.getIdEstudiante())
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado para la consulta"));

        Date fechaAgendada = new Date(fechaHoraTimestamp);

        consulta.setFechaLlamadaAgendada(fechaAgendada);
        consulta.setEstado("Agendada");
        consulta.setIdDocenteAsignado(docente.getId());
        Consulta consultaActualizada = consultaRepository.save(consulta);

        emailService.sendSchedulingConfirmationEmail(
            estudiante.getEmail(),
            estudiante.getEmailTutor(),
            estudiante.getNombreCompleto(),
            docente.getNombreCompleto(),
            fechaAgendada,
            consulta.getTitulo()
        );

        return consultaActualizada;
    }

    @Transactional
    public InformeSesion createInformeAndUpdateConsulta(CreateInformeDTO informeDTO, Long consultaId, String docenteEmail) {
        Usuario docente = usuarioRepository.findByEmail(docenteEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Docente no encontrado"));

        Consulta consulta = consultaRepository.findById(consultaId)
            .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));
            
        Usuario estudiante = usuarioRepository.findById(consulta.getIdEstudiante())
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado para la consulta"));

        InformeSesion nuevoInforme = new InformeSesion();
        nuevoInforme.setIdConsulta(consultaId);
        nuevoInforme.setIdDocente(docente.getId());
        nuevoInforme.setFechaCreacion(new Date());

        nuevoInforme.setPuntosFuertes(informeDTO.getObservacionesDocente());
        nuevoInforme.setParticipacionGeneral(informeDTO.getParticipacionGeneral());
        nuevoInforme.setCumplimientoTareas(informeDTO.getCumplimientoTareas());
        nuevoInforme.setNivelAvance(informeDTO.getNivelAvance());
        nuevoInforme.setDesempenoPromedio(informeDTO.getDesempenoPromedio());
        nuevoInforme.setResolucionProblemas(informeDTO.getResolucionProblemas());
        nuevoInforme.setRazonamientoLogico(informeDTO.getRazonamientoLogico());
        nuevoInforme.setCalculoOperaciones(informeDTO.getCalculoOperaciones());
        nuevoInforme.setParticipacionPlataforma(informeDTO.getParticipacionPlataforma());
        
        nuevoInforme.setAreasAMejorar("N/A");
        nuevoInforme.setRecomendaciones("N/A");
        
        informeSesionRepository.save(nuevoInforme);

        consulta.setEstado("Respondida");
        consultaRepository.save(consulta);

        try {
            byte[] pdfContent = pdfService.generarInformePdf(nuevoInforme, estudiante, docente, consulta);
            emailService.sendInformeNotificationEmail(
                estudiante.getEmail(),
                estudiante.getEmailTutor(),
                estudiante.getNombreCompleto(),
                pdfContent
            );
        } catch (Exception e) {
            System.err.println("El informe se guardó, pero hubo un error al enviar el correo: " + e.getMessage());
        }

        return nuevoInforme;
    }
    
    @Transactional
    public Consulta responderConsultaTexto(Long consultaId, String textoRespuesta, String docenteEmail) {
        Usuario docente = usuarioRepository.findByEmail(docenteEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Docente no encontrado"));

        Consulta consulta = consultaRepository.findById(consultaId)
            .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        consulta.setRespuestaTexto(textoRespuesta);
        consulta.setEstado("Respondida");
        consulta.setIdDocenteAsignado(docente.getId());
        
        return consultaRepository.save(consulta);
    }

    public Consulta iniciarLlamada(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
            .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        String nombreSala = "TutorIA-" + consultaId + "-" + UUID.randomUUID().toString().substring(0, 8);
        
        consulta.setSalaLlamada(nombreSala);
        consulta.setEstado("Llamada en curso");
        
        return consultaRepository.save(consulta);
    }
}