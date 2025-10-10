package com.gestion.emprendedora.services;

import com.gestion.emprendedora.dto.CreateHiloDTO;
import com.gestion.emprendedora.dto.CreateRespuestaDTO;
import com.gestion.emprendedora.dto.HiloConRespuestasDTO;
import com.gestion.emprendedora.dto.RespuestaConUsuarioDTO;
import com.gestion.emprendedora.entities.ForoHilo;
import com.gestion.emprendedora.entities.ForoRespuesta;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.ForoHiloRepository;
import com.gestion.emprendedora.repositories.ForoRespuestaRepository;
import com.gestion.emprendedora.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForoService {

    @Autowired private ForoHiloRepository foroHiloRepository;
    @Autowired private ForoRespuestaRepository foroRespuestaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<ForoHilo> getAllHilos() {
        // Devuelve los hilos ordenados por el más reciente primero
        return foroHiloRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public HiloConRespuestasDTO getHiloConRespuestas(Long hiloId) {
        ForoHilo hilo = foroHiloRepository.findById(hiloId).orElseThrow(() -> new RuntimeException("Hilo no encontrado con id: " + hiloId));
        Usuario autorHilo = usuarioRepository.findById(hilo.getIdUsuario()).orElse(null);
        
        List<ForoRespuesta> respuestas = foroRespuestaRepository.findByIdHilo(hiloId);
        
        List<RespuestaConUsuarioDTO> respuestasConUsuario = respuestas.stream().map(respuesta -> {
            Usuario autorRespuesta = usuarioRepository.findById(respuesta.getIdUsuario()).orElse(null);
            return new RespuestaConUsuarioDTO(
                respuesta,
                autorRespuesta != null ? autorRespuesta.getNombreCompleto() : "Usuario Desconocido",
                autorRespuesta != null ? autorRespuesta.getRol() : "N/A"
            );
        }).collect(Collectors.toList());

        return new HiloConRespuestasDTO(hilo, autorHilo != null ? autorHilo.getNombreCompleto() : "Usuario Desconocido", respuestasConUsuario);
    }

    public ForoHilo createHilo(CreateHiloDTO hiloDTO, String userEmail) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        ForoHilo nuevoHilo = new ForoHilo();
        nuevoHilo.setTitulo(hiloDTO.getTitulo());
        nuevoHilo.setContenido(hiloDTO.getContenido());
        nuevoHilo.setCategoria(hiloDTO.getCategoria());
        nuevoHilo.setIdUsuario(usuario.getId());
        nuevoHilo.setFechaCreacion(new Date());
        return foroHiloRepository.save(nuevoHilo);
    }

    public ForoRespuesta createRespuesta(CreateRespuestaDTO respuestaDTO, Long hiloId, String userEmail) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        ForoRespuesta nuevaRespuesta = new ForoRespuesta();
        nuevaRespuesta.setContenido(respuestaDTO.getContenido());
        nuevaRespuesta.setIdHilo(hiloId);
        nuevaRespuesta.setIdUsuario(usuario.getId());
        nuevaRespuesta.setFechaCreacion(new Date());
        return foroRespuestaRepository.save(nuevaRespuesta);
    }
}

