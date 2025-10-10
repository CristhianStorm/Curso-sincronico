package com.gestion.emprendedora.repositories;

import com.gestion.emprendedora.entities.ForoHilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad ForoHilo.
 * Proporciona los métodos para interactuar con la tabla FORO_HILOS en la base de datos.
 */
@Repository
public interface ForoHiloRepository extends JpaRepository<ForoHilo, Long> {
    // Por ahora, los métodos básicos de JpaRepository (findAll, findById, save, etc.) son suficientes.
    // En el futuro, podríamos añadir métodos personalizados aquí si los necesitamos.
}
