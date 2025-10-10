package com.gestion.emprendedora.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Table(name = "INFORMES_SESION")
@Data
public class InformeSesion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INFORMES_SEQ")
    @SequenceGenerator(name = "INFORMES_SEQ", sequenceName = "INFORMES_SEQ", allocationSize = 1)
    @Column(name = "ID_INFORME")
    private Long id;

    @Column(name = "ID_CONSULTA", nullable = false)
    private Long idConsulta;
    
    @Column(name = "ID_DOCENTE", nullable = false)
    private Long idDocente;

    @Lob // Para textos largos (CLOB)
    @Column(name = "PUNTOS_FUERTES")
    private String puntosFuertes;

    @Lob
    @Column(name = "AREAS_A_MEJORAR")
    private String areasAMejorar;

    @Lob
    @Column(name = "RECOMENDACIONES")
    private String recomendaciones;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", updatable = false)
    @ColumnDefault("SYSDATE")
    private Date fechaCreacion;
}
