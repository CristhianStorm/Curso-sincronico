package com.gestion.emprendedora.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "ARCHIVOS_ADJUNTOS")
@Data
public class ArchivoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARCHIVOS_SEQ")
    @SequenceGenerator(name = "ARCHIVOS_SEQ", sequenceName = "ARCHIVOS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "ID_CONSULTA", nullable = false)
    private Long idConsulta;

    @Column(name = "ID_USUARIO_SUBIO", nullable = false)
    private Long idUsuarioQueSubio;

    @Column(name = "NOMBRE_ORIGINAL", nullable = false)
    private String nombreOriginal;

    @Column(name = "NOMBRE_GUARDADO", nullable = false, unique = true)
    private String nombreGuardado; // Un nombre único para evitar colisiones

    @Column(name = "TIPO_MIME")
    private String tipoMime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSubida = new Date();
}