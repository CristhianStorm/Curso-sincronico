package com.gestion.emprendedora.repositories;

import com.gestion.emprendedora.entities.InformeSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InformeSesionRepository extends JpaRepository<InformeSesion, Long> {

    // Método para buscar un informe por el ID de la consulta a la que pertenece
    Optional<InformeSesion> findByIdConsulta(Long idConsulta);
}
