package com.gestion.emprendedora.repositories;

import com.gestion.emprendedora.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    // Spring Data JPA creará automáticamente un método para buscar todas las consultas
    // que pertenezcan a un ID de estudiante específico.
    List<Consulta> findByIdEstudianteOrderByIdDesc(Long idEstudiante);
}
