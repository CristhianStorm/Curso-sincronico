package com.gestion.emprendedora.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para manejar los datos del formulario de registro.
 * Incluye campos para el estudiante y, opcionalmente, para el tutor.
 */
@Data
public class RegistroRequestDTO {

    // --- Datos del Estudiante ---
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombreCompleto;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ser un formato de email válido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotNull(message = "La edad no puede estar vacía")
    private Integer edad;

    @NotBlank(message = "La cédula no puede estar vacía")
    private String cedula;
    
    // --- ¡CAMPOS AÑADIDOS! Datos del Tutor (opcionales) ---
    private String nombreTutor;
    
    @Email(message = "Debe ser un formato de email válido para el tutor")
    private String emailTutor;
    
    private String parentesco;
}

