package com.gestion.emprendedora.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    private String nombreCompleto;
    private String email;
    private String rol;

    public UserInfoDTO(String nombreCompleto, String email, String rol) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.rol = rol;
    }
}
