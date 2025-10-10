    package com.gestion.emprendedora.dto;

    import com.gestion.emprendedora.entities.ForoRespuesta;
    import lombok.Data;

    @Data
    public class RespuestaConUsuarioDTO {
        private ForoRespuesta respuesta;
        private String nombreUsuario;
        private String rolUsuario;

        public RespuestaConUsuarioDTO(ForoRespuesta respuesta, String nombreUsuario, String rolUsuario) {
            this.respuesta = respuesta;
            this.nombreUsuario = nombreUsuario;
            this.rolUsuario = rolUsuario;
        }
    }
    
