package com.gestion.emprendedora.repositories;

import com.gestion.emprendedora.entities.RegistroPendiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la entidad RegistroPendiente.
 * Proporciona los métodos para interactuar con la tabla REGISTROS_PENDIENTES en la base de datos.
 */
@Repository
public interface RegistroPendienteRepository extends JpaRepository<RegistroPendiente, Long> {

    /**
     * Busca un registro pendiente por su token de verificación único.
     * Este método es crucial para encontrar el registro correcto cuando el tutor
     * hace clic en el enlace de su correo electrónico.
     * @param token El token de verificación a buscar.
     * @return Un Optional que contiene el RegistroPendiente si se encuentra.
     */
    Optional<RegistroPendiente> findByTokenVerificacion(String token);
}

