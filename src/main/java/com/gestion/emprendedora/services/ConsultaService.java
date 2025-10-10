package com.gestion.emprendedora.services;

import com.gestion.emprendedora.dto.CreateConsultaDTO;
import com.gestion.emprendedora.entities.Consulta;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.ConsultaRepository;
import com.gestion.emprendedora.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date; // Asegúrate de tener este import
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Busca las consultas de un usuario específico por su email.
     * @param email El email del usuario autenticado.
     * @return Una lista de sus consultas, ordenadas por la más reciente primero.
     */
    public List<Consulta> findConsultasByEmail(String email) {
        // Primero, encontramos al usuario por su email para obtener su ID.
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        
        // Luego, usamos ese ID para buscar todas sus consultas.
        return consultaRepository.findByIdEstudianteOrderByIdDesc(usuario.getId());
    }

    /**
     * Crea una nueva consulta para un usuario.
     * @param consultaDTO Los datos de la nueva consulta desde el formulario.
     * @param email El email del usuario autenticado.
     * @return La consulta recién guardada en la base de datos.
     */
    public Consulta createConsulta(CreateConsultaDTO consultaDTO, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        
        Consulta nuevaConsulta = new Consulta();
        nuevaConsulta.setIdEstudiante(usuario.getId());
        nuevaConsulta.setMateria(consultaDTO.getMateria());
        nuevaConsulta.setTitulo(consultaDTO.getTitulo());
        nuevaConsulta.setDescripcion(consultaDTO.getDescripcion());
        nuevaConsulta.setSolicitaLlamada(consultaDTO.isSolicitaLlamada() ? 'S' : 'N');
        nuevaConsulta.setEstado("Pendiente");
        nuevaConsulta.setFechaCreacion(new Date()); // Asignamos la fecha actual

        return consultaRepository.save(nuevaConsulta);
    }
}

