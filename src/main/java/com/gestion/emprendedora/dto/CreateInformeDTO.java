package com.gestion.emprendedora.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateInformeDTO {

    @NotBlank
    private String puntosFuertes;

    @NotBlank
    private String areasAMejorar;

    private String recomendaciones;
}
