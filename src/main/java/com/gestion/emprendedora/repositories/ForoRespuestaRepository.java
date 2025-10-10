package com.gestion.emprendedora.repositories;

import com.gestion.emprendedora.entities.ForoRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad ForoRespuesta.
 * Proporciona los métodos para interactuar con la tabla FORO_RESPUESTAS en la base de datos.
 */
@Repository
public interface ForoRespuestaRepository extends JpaRepository<ForoRespuesta, Long> {

    /**
     * Busca todas las respuestas que pertenecen a un hilo específico.
     * Spring Data JPA implementará este método automáticamente.
     * @param idHilo El ID del hilo del que se quieren obtener las respuestas.
     * @return Una lista de respuestas para el hilo dado.
     */
    List<ForoRespuesta> findByIdHilo(Long idHilo);
}
