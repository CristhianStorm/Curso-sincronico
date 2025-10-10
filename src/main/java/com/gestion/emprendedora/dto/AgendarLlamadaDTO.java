package com.gestion.emprendedora.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
// Ya no necesitas importar java.util.Date aquí

@Data
public class AgendarLlamadaDTO {
    @NotNull
    private Long fechaHora; // <-- ¡CAMBIO AQUÍ! De Date a Long
}