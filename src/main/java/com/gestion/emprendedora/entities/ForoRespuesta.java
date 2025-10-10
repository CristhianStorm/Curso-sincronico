package com.gestion.emprendedora.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import java.util.Date;

@Entity
@Table(name = "FORO_RESPUESTAS")
@Data
public class ForoRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESPUESTAS_SEQ")
    @SequenceGenerator(name = "RESPUESTAS_SEQ", sequenceName = "RESPUESTAS_SEQ", allocationSize = 1)
    @Column(name = "ID_RESPUESTA")
    private Long id;

    @Column(name = "ID_HILO", nullable = false)
    private Long idHilo;

    @Column(name = "ID_USUARIO", nullable = false)
    private Long idUsuario;

    @Lob
    @Column(name = "CONTENIDO")
    private String contenido;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", updatable = false)
    @ColumnDefault("SYSDATE")
    private Date fechaCreacion;
}
