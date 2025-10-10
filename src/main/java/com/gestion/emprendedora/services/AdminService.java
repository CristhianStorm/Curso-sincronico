package com.gestion.emprendedora.services;

import com.gestion.emprendedora.dto.CreateUserDTO;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> getPendingUsers() {
        return usuarioRepository.findByEstado("pendiente_pago");
    }

    public Usuario activateUser(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));
        
        usuario.setEstado("activo");
        return usuarioRepository.save(usuario);
    }
    
    public Usuario createUser(CreateUserDTO userDTO) {
        if (usuarioRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Error: El email ya está en uso!");
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreCompleto(userDTO.getNombreCompleto());
        nuevoUsuario.setEmail(userDTO.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        nuevoUsuario.setRol(userDTO.getRol().toLowerCase());
        nuevoUsuario.setEstado("activo");
        nuevoUsuario.setFechaRegistro(new Date());
        return usuarioRepository.save(nuevoUsuario);
    }
}

