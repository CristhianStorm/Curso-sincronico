package com.gestion.emprendedora.services;

import com.gestion.emprendedora.entities.InformeSesion;
import com.gestion.emprendedora.repositories.InformeSesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InformeService {

    @Autowired
    private InformeSesionRepository informeSesionRepository;

    /**
     * Busca un informe asociado a un ID de consulta específico.
     * @param consultaId El ID de la consulta.
     * @return El informe de la sesión si existe.
     */
    public InformeSesion getInformeByConsultaId(Long consultaId) {
        return informeSesionRepository.findByIdConsulta(consultaId)
            .orElseThrow(() -> new RuntimeException("No se encontró un informe para esta consulta."));
    }
}
