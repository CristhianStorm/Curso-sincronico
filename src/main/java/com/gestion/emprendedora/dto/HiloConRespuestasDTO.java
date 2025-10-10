    package com.gestion.emprendedora.dto;

    import com.gestion.emprendedora.entities.ForoHilo;
    import lombok.Data;
    import java.util.List;

    @Data
    public class HiloConRespuestasDTO {
        private ForoHilo hilo;
        private String nombreAutor;
        private List<RespuestaConUsuarioDTO> respuestas;

        public HiloConRespuestasDTO(ForoHilo hilo, String nombreAutor, List<RespuestaConUsuarioDTO> respuestas) {
            this.hilo = hilo;
            this.nombreAutor = nombreAutor;
            this.respuestas = respuestas;
        }
    }
    
