package com.gestion.emprendedora.dto;

import lombok.Data;

@Data
public class CreateInformeDTO {
    
    // El campo de observaciones ahora mapea a "puntosFuertes" en la entidad
    private String observacionesDocente; 
    
    private String participacionGeneral;
    private String cumplimientoTareas;
    private String nivelAvance;
    private String desempenoPromedio;
    
    private String resolucionProblemas;
    private String razonamientoLogico;
    private String calculoOperaciones;
    private String participacionPlataforma;
}