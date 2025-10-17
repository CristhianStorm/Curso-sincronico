package com.gestion.emprendedora.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

/**
 * Entidad que representa la tabla CONSULTAS en la base de datos.
 * Almacena la información de cada pregunta realizada por un estudiante.
 */
@Entity
@Table(name = "CONSULTAS")
@Data
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONSULTAS_SEQ")
    @SequenceGenerator(name = "CONSULTAS_SEQ", sequenceName = "CONSULTAS_SEQ", allocationSize = 1)
    @Column(name = "ID_CONSULTA")
    private Long id;

    @Column(name = "ID_ESTUDIANTE", nullable = false)
    private Long idEstudiante;

    @Column(name = "ID_DOCENTE_ASIGNADO")
    private Long idDocenteAsignado;

    @Column(name = "MATERIA")
    private String materia;

    @Column(name = "TITULO")
    private String titulo;

    @Lob // Usamos @Lob para campos CLOB que pueden ser muy largos
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "SOLICITA_LLAMADA")
    private char solicitaLlamada = 'N';

    @Column(name = "ESTADO")
    private String estado;

    /**
     * Almacena el nombre único de la sala de videollamada (ej: de Jitsi).
     */
    @Column(name = "SALA_LLAMADA")
    private String salaLlamada;

    /**
     * Almacena la fecha y hora exactas en que se ha programado una videollamada.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_LLAMADA_AGENDADA")
    private Date fechaLlamadaAgendada;

    /**
     * ¡NUEVO CAMPO!
     * Almacena la respuesta de texto proporcionada por un docente.
     */
    @Lob 
    @Column(name = "RESPUESTA_TEXTO")
    private String respuestaTexto;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", updatable = false)
    @ColumnDefault("SYSDATE")
    private Date fechaCreacion;
}