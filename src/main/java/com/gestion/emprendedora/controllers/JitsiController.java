package com.gestion.emprendedora.controllers;

import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.UsuarioRepository;
import com.gestion.emprendedora.services.JaaS_TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/jitsi")
public class JitsiController {

    @Autowired
    private JaaS_TokenService jaasTokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> getJitsiToken(Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
        String token = jaasTokenService.generarToken(usuario);

        return ResponseEntity.ok(Map.of("token", token));
    }
}