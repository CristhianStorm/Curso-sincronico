package com.gestion.emprendedora.controllers;

import com.gestion.emprendedora.config.JwtUtil;
import com.gestion.emprendedora.dto.JwtResponseDTO;
import com.gestion.emprendedora.dto.LoginRequestDTO;
import com.gestion.emprendedora.dto.RegistroRequestDTO;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.services.AuthService;
import com.gestion.emprendedora.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para manejar las peticiones de autenticación.
 * Usa @Controller para poder redirigir a páginas HTML de verificación.
 */
@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Endpoint para registrar un nuevo usuario.
     * Si el usuario es menor de edad, inicia el flujo de verificación del tutor.
     */
    @PostMapping("/registrar")
    @ResponseBody // Indica que este método devuelve datos (JSON), no una vista HTML.
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegistroRequestDTO registroRequest) {
        try {
            authService.registrarUsuario(registroRequest);
            return ResponseEntity.ok("Registro procesado. Por favor, siga las instrucciones.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para autenticar un usuario y obtener un token JWT junto con su rol y estado.
     */
    @PostMapping("/login")
    @ResponseBody // Indica que este método devuelve datos (JSON).
    public ResponseEntity<?> loginUsuario(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error: Credenciales inválidas");
        }

        // Como nuestro UserDetailsServiceImpl ahora devuelve un objeto Usuario, podemos hacer un cast
        Usuario userDetails = (Usuario) userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        String role = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse(null);
        
        if (role != null && role.startsWith("ROLE_")) {
            role = role.substring(5).toLowerCase();
        }

        // Devolvemos el token, el rol y ahora también el estado
        return ResponseEntity.ok(new JwtResponseDTO(jwt, role, userDetails.getEstado()));
    }
    
    /**
     * ¡NUEVO ENDPOINT!
     * Este es el que el tutor visitará desde el enlace en su correo electrónico.
     * @param token El token de verificación único.
     * @return Una redirección a una página de éxito o de fallo.
     */
    @GetMapping("/verificar")
    public String verificarCuenta(@RequestParam("token") String token) {
        try {
            authService.verificarToken(token);
            // Si el token es válido, redirige a la página de éxito.
            return "redirect:/verificacion_exitosa.html";
        } catch (Exception e) {
            // Si el token es inválido o ha expirado, redirige a la página de error.
            return "redirect:/verificacion_fallida.html";
        }
    }
}

