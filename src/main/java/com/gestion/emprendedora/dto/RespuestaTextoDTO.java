package com.gestion.emprendedora.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RespuestaTextoDTO {
    @NotBlank
    private String respuesta;
}