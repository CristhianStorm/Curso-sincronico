    package com.gestion.emprendedora.dto;

    import jakarta.validation.constraints.NotBlank;
    import lombok.Data;

    @Data
    public class CreateHiloDTO {
        @NotBlank
        private String titulo;
        private String contenido;
        @NotBlank
        private String categoria;
    }
    
