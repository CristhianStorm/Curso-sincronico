package com.gestion.emprendedora.repositories;

import com.gestion.emprendedora.entities.ArchivoAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchivoAdjuntoRepository extends JpaRepository<ArchivoAdjunto, Long> {

    /**
     * Busca todos los archivos que pertenecen a una consulta específica.
     * Spring Data JPA implementará este método automáticamente.
     * @param idConsulta El ID de la consulta.
     * @return Una lista de archivos adjuntos.
     */
    List<ArchivoAdjunto> findByIdConsulta(Long idConsulta);

    /**
     * ¡ESTE ES EL MÉTODO NUEVO!
     * Busca un registro de archivo por su nombre único guardado en el servidor.
     * Es necesario para que, al descargar, podamos encontrar el nombre original
     * del archivo y ofrecérselo al usuario.
     * @param nombreGuardado El nombre del archivo generado con UUID.
     * @return Un Optional que contiene el ArchivoAdjunto si se encuentra.
     */
    Optional<ArchivoAdjunto> findByNombreGuardado(String nombreGuardado);
}