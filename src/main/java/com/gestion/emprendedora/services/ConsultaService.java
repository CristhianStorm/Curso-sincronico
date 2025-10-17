package com.gestion.emprendedora.services;

import com.gestion.emprendedora.dto.CreateConsultaDTO;
import com.gestion.emprendedora.entities.Consulta;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.ConsultaRepository;
import com.gestion.emprendedora.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional; // ¡Importante añadir esta línea!

import java.util.Date;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Busca las consultas de un usuario específico por su email.
     */
    public List<Consulta> findConsultasByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        
        return consultaRepository.findByIdEstudianteOrderByIdDesc(usuario.getId());
    }

    /**
     * ¡MÉTODO UNIFICADO Y CORREGIDO!
     * Crea una nueva consulta y, si se proporciona un archivo, lo guarda y asocia.
     * @Transactional asegura que o se guardan ambos (consulta y archivo) o no se guarda nada.
     */
    @Transactional
    public Consulta createConsulta(CreateConsultaDTO consultaDTO, String email, MultipartFile file) {
        // 1. Buscamos al usuario que crea la consulta
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        
        // 2. Creamos y populamos la nueva entidad Consulta
        Consulta nuevaConsulta = new Consulta();
        nuevaConsulta.setIdEstudiante(usuario.getId());
        nuevaConsulta.setMateria(consultaDTO.getMateria());
        nuevaConsulta.setTitulo(consultaDTO.getTitulo());
        nuevaConsulta.setDescripcion(consultaDTO.getDescripcion());
        nuevaConsulta.setSolicitaLlamada(consultaDTO.isSolicitaLlamada() ? 'S' : 'N');
        nuevaConsulta.setEstado("Pendiente");
        nuevaConsulta.setFechaCreacion(new Date());

        // 3. Guardamos la consulta para obtener su ID
        Consulta consultaGuardada = consultaRepository.save(nuevaConsulta);

        // 4. Si el usuario adjuntó un archivo, lo guardamos usando el FileStorageService
        if (file != null && !file.isEmpty()) {
            // Este método ya se encarga de crear el registro en la tabla ArchivoAdjunto
            fileStorageService.guardarArchivo(file, consultaGuardada.getId(), usuario);
        }

        // 5. Devolvemos la consulta recién creada
        return consultaGuardada;
    }
}