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

    // --- CAMPOS ANTIGUOS (REUTILIZADOS) ---
    @Lob 
    @Column(name = "PUNTOS_FUERTES")
    private String puntosFuertes; // Se reutiliza como "Observaciones del Docente"

    @Lob
    @Column(name = "AREAS_A_MEJORAR")
    private String areasAMejorar; // Se puede eliminar si no se usa, o mantenerlo. Lo quitaremos en el DTO.

    @Lob
    @Column(name = "RECOMENDACIONES")
    private String recomendaciones; // Se puede eliminar si no se usa. Lo quitaremos en el DTO.

    // --- ¡NUEVOS CAMPOS DEL INFORME DETALLADO! ---
    @Column(name = "PARTICIPACION_GENERAL")
    private String participacionGeneral; // Alta, Media, Baja

    @Column(name = "CUMPLIMIENTO_TAREAS")
    private String cumplimientoTareas; // Ej. "90% completadas"

    @Column(name = "NIVEL_AVANCE")
    private String nivelAvance; // Ej. "Álgebra básica, fracciones"

    @Column(name = "DESEMPENO_PROMEDIO")
    private String desempenoPromedio; // Ej. "85% de respuestas correctas"

    @Column(name = "RESOLUCION_PROBLEMAS")
    private String resolucionProblemas; // Excelente, Bueno, Regular, En proceso

    @Column(name = "RAZONAMIENTO_LOGICO")
    private String razonamientoLogico;

    @Column(name = "CALCULO_OPERACIONES")
    private String calculoOperaciones;

    @Column(name = "PARTICIPACION_PLATAFORMA")
    private String participacionPlataforma;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", updatable = false)
    @ColumnDefault("SYSDATE")
    private Date fechaCreacion;
}