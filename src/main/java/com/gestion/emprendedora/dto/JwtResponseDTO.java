package com.gestion.emprendedora.dto;

import lombok.Data;

/**
 * DTO para la respuesta de una autenticación exitosa.
 * Contiene el token, el rol y el estado del usuario para ser usados por el frontend.
 */
@Data
public class JwtResponseDTO {
    
    private String token;
    private String role;
    private String estado; // ¡CAMPO AÑADIDO!

    /**
     * Constructor actualizado para incluir el estado del usuario.
     * @param token El JSON Web Token generado.
     * @param role El rol del usuario (ej: "estudiante").
     * @param estado El estado actual de la cuenta (ej: "pendiente_pago").
     */
    public JwtResponseDTO(String token, String role, String estado) {
        this.token = token;
        this.role = role;
        this.estado = estado;
    }
}

