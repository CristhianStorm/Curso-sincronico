package com.gestion.emprendedora.repositories;

import com.gestion.emprendedora.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona los métodos para interactuar con la tabla USUARIOS en la base de datos.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su dirección de correo electrónico.
     * Spring Data JPA implementará este método automáticamente.
     * @param email El email del usuario a buscar.
     * @return Un Optional que contiene el Usuario si se encuentra, o vacío si no.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca todos los usuarios que coinciden con un estado específico.
     * Utilizado por el administrador para encontrar usuarios pendientes.
     * @param estado El estado a buscar (ej: "pendiente_pago").
     * @return Una lista de usuarios que coinciden con el estado.
     */
    List<Usuario> findByEstado(String estado);
}

