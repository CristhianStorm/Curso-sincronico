package com.gestion.emprendedora.controllers;

import com.gestion.emprendedora.config.JwtUtil;
import com.gestion.emprendedora.dto.JwtResponseDTO;
import com.gestion.emprendedora.dto.LoginRequestDTO;
import com.gestion.emprendedora.dto.RegistroRequestDTO;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.RegistroPendienteRepository; // ¡Importante añadir!
import com.gestion.emprendedora.services.AuthService;
import com.gestion.emprendedora.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException; // ¡Importante añadir!
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    // --- ¡NUEVA INYECCIÓN! ---
    @Autowired
    private RegistroPendienteRepository registroPendienteRepository;

    @PostMapping("/registrar")
    @ResponseBody
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegistroRequestDTO registroRequest) {
        // ... (código sin cambios)
        try {
            authService.registrarUsuario(registroRequest);
            return ResponseEntity.ok("Registro procesado. Por favor, siga las instrucciones.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * ¡MÉTODO COMPLETAMENTE ACTUALIZADO!
     * Ahora maneja 3 casos de error distintos:
     * 1. Cuenta pendiente de activación del tutor.
     * 2. Cuenta pendiente de activación del administrador.
     * 3. Credenciales incorrectas.
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginUsuario(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (DisabledException e) {
            // Este error ocurre si la cuenta existe pero isEnabled() devuelve false (estado != 'activo').
            return ResponseEntity.status(401).body("Error: Tu cuenta está pendiente de activación por un administrador.");
        } catch (BadCredentialsException e) {
            // Este error ocurre si el usuario no se encuentra o la contraseña es incorrecta.
            // Verificamos si es porque está pendiente de la activación del tutor.
            if (registroPendienteRepository.findByEmail(loginRequest.getEmail()).isPresent()) {
                return ResponseEntity.status(401).body("Error: Tu cuenta está pendiente. Pídele a tu tutor que la active desde el correo de verificación.");
            }
            // Si no, es simplemente un error de credenciales.
            return ResponseEntity.status(401).body("Error: Correo o contraseña inválidos.");
        }

        // Si la autenticación es exitosa, el resto del código se ejecuta
        Usuario userDetails = (Usuario) userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        String role = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse(null);
        
        if (role != null && role.startsWith("ROLE_")) {
            role = role.substring(5).toLowerCase();
        }

        return ResponseEntity.ok(new JwtResponseDTO(jwt, role, userDetails.getEstado()));
    }
    
    @GetMapping("/verificar")
    public String verificarCuenta(@RequestParam("token") String token) {
        // ... (código sin cambios)
        try {
            authService.verificarToken(token);
            return "redirect:/verificacion_exitosa.html";
        } catch (Exception e) {
            return "redirect:/verificacion_fallida.html";
        }
    }
}