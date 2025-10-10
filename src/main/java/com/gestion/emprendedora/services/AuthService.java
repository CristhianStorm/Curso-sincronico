package com.gestion.emprendedora.services;

import com.gestion.emprendedora.dto.RegistroRequestDTO;
import com.gestion.emprendedora.entities.RegistroPendiente;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.RegistroPendienteRepository;
import com.gestion.emprendedora.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Servicio que maneja la lógica de negocio para la autenticación y registro de usuarios,
 * incluyendo el flujo de verificación para tutores de menores de edad.
 */
@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RegistroPendienteRepository registroPendienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;

    /**
     * Registra un nuevo usuario. Si es menor de edad, inicia el proceso de verificación del tutor.
     * Si es mayor de edad, crea la cuenta directamente.
     * @param registroRequest Los datos del usuario que vienen del formulario.
     */
    public void registrarUsuario(RegistroRequestDTO registroRequest) {
        // 1. Verificamos que el email del estudiante no esté ya en uso.
        if (usuarioRepository.findByEmail(registroRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Error: El email del estudiante ya está en uso!");
        }

        // 2. Comprobamos la edad del estudiante para decidir el flujo.
        if (registroRequest.getEdad() < 18) {
            // --- FLUJO PARA MENORES DE EDAD ---
            String token = UUID.randomUUID().toString();
            
            // Creamos un registro temporal en la tabla REGISTROS_PENDIENTES.
            RegistroPendiente pendiente = new RegistroPendiente();
            pendiente.setNombreCompleto(registroRequest.getNombreCompleto());
            pendiente.setEmail(registroRequest.getEmail());
            pendiente.setPasswordHash(passwordEncoder.encode(registroRequest.getPassword()));
            pendiente.setEdad(registroRequest.getEdad());
            pendiente.setCedula(registroRequest.getCedula());
            pendiente.setEmailTutor(registroRequest.getEmailTutor());
            pendiente.setTokenVerificacion(token);
            // El token será válido por 24 horas.
            pendiente.setFechaExpiracion(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
            
            registroPendienteRepository.save(pendiente);

            // Llamamos al EmailService para enviar un correo real.
            String verificationLink = "http://localhost:8080/api/auth/verificar?token=" + token;
            emailService.sendVerificationEmail(
                registroRequest.getEmailTutor(),
                registroRequest.getNombreCompleto(),
                verificationLink
            );

        } else {
            // --- FLUJO PARA MAYORES DE EDAD (sin cambios) ---
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreCompleto(registroRequest.getNombreCompleto());
            nuevoUsuario.setEmail(registroRequest.getEmail());
            nuevoUsuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));
            nuevoUsuario.setEdad(registroRequest.getEdad());
            nuevoUsuario.setCedula(registroRequest.getCedula());
            nuevoUsuario.setRol("estudiante");
            nuevoUsuario.setEstado("pendiente_pago");
            nuevoUsuario.setFechaRegistro(new Date());
            usuarioRepository.save(nuevoUsuario);
        }
    }
    
    /**
     * Verifica un token de tutor, crea la cuenta del estudiante y elimina el registro pendiente.
     * @param token El token de verificación único enviado al correo del tutor.
     * @return Un mensaje de éxito.
     */
    public String verificarToken(String token) {
        // Buscamos el registro pendiente usando el token.
        RegistroPendiente pendiente = registroPendienteRepository.findByTokenVerificacion(token)
            .orElseThrow(() -> new RuntimeException("Token inválido o ya utilizado."));

        // Verificamos si el token ha expirado.
        if (pendiente.getFechaExpiracion().before(new Date())) {
            registroPendienteRepository.delete(pendiente);
            throw new RuntimeException("El token de verificación ha expirado.");
        }

        // Si el token es válido, creamos la cuenta del usuario final.
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreCompleto(pendiente.getNombreCompleto());
        nuevoUsuario.setEmail(pendiente.getEmail());
        nuevoUsuario.setPassword(pendiente.getPasswordHash());
        nuevoUsuario.setEdad(pendiente.getEdad());
        nuevoUsuario.setCedula(pendiente.getCedula());
        nuevoUsuario.setRol("estudiante");
        nuevoUsuario.setEstado("pendiente_pago");
        nuevoUsuario.setFechaRegistro(new Date());
        
        // --- ¡CAMBIO IMPORTANTE! ---
        // Guardamos el email del tutor en la cuenta del estudiante.
        nuevoUsuario.setEmailTutor(pendiente.getEmailTutor());

        usuarioRepository.save(nuevoUsuario);

        // Eliminamos el registro pendiente para que el token no pueda ser usado de nuevo.
        registroPendienteRepository.delete(pendiente);
        return "¡Cuenta activada exitosamente! Ahora el estudiante puede proceder al pago.";
    }
}