package com.gestion.emprendedora.controllers;

import com.gestion.emprendedora.dto.UserInfoDTO;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> getCurrentUser(Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        UserInfoDTO userInfo = new UserInfoDTO(
            usuario.getNombreCompleto(),
            usuario.getEmail(),
            usuario.getRol()
        );
        return ResponseEntity.ok(userInfo);
    }
}
