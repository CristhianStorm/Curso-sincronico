    package com.gestion.emprendedora.dto;

    import jakarta.validation.constraints.NotBlank;
    import lombok.Data;

    @Data
    public class CreateRespuestaDTO {
        @NotBlank
        private String contenido;
    }
    
