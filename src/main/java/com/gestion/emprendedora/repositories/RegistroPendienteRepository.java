package com.gestion.emprendedora.repositories;

import com.gestion.emprendedora.entities.RegistroPendiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la entidad RegistroPendiente.
 */
@Repository
public interface RegistroPendienteRepository extends JpaRepository<RegistroPendiente, Long> {

    /**
     * Busca un registro pendiente por su token de verificación único.
     */
    Optional<RegistroPendiente> findByTokenVerificacion(String token);

    /**
     * ¡NUEVO MÉTODO!
     * Busca un registro pendiente por el email del estudiante.
     * Nos permitirá saber si un intento de login fallido es de una cuenta
     * que está esperando la verificación del tutor.
     */
    Optional<RegistroPendiente> findByEmail(String email);
}