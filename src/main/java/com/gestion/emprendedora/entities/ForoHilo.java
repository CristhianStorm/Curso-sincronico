package com.gestion.emprendedora.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import java.util.Date;

@Entity
@Table(name = "FORO_HILOS")
@Data
public class ForoHilo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HILOS_SEQ")
    @SequenceGenerator(name = "HILOS_SEQ", sequenceName = "HILOS_SEQ", allocationSize = 1)
    @Column(name = "ID_HILO")
    private Long id;

    @Column(name = "ID_USUARIO", nullable = false)
    private Long idUsuario;

    @Column(name = "TITULO", nullable = false)
    private String titulo;

    @Lob
    @Column(name = "CONTENIDO")
    private String contenido;

    @Column(name = "CATEGORIA")
    private String categoria;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", updatable = false)
    @ColumnDefault("SYSDATE")
    private Date fechaCreacion;
}
