    package com.gestion.emprendedora.dto;

    import jakarta.validation.constraints.NotBlank;
    import lombok.Data;

    @Data
    public class CreateConsultaDTO {
        @NotBlank
        private String materia;
        @NotBlank
        private String titulo;
        private String descripcion;
        private boolean solicitaLlamada;
    }
    
