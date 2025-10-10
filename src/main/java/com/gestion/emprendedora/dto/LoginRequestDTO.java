package com.gestion.emprendedora.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "El email no puede estar vacío")
    @Email
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}
