package com.gestion.emprendedora.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "REGISTROS_PENDIENTES")
@Data
public class RegistroPendiente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REG_PEND_SEQ")
    @SequenceGenerator(name = "REG_PEND_SEQ", sequenceName = "REG_PEND_SEQ", allocationSize = 1)
    private Long id;

    // Datos del estudiante
    private String nombreCompleto;
    private String email;
    private String passwordHash;
    private Integer edad;
    private String cedula;
    
    // Datos del tutor
    private String emailTutor;
    
    // Datos para la verificación
    @Column(unique = true)
    private String tokenVerificacion;
    private Date fechaExpiracion;
}

