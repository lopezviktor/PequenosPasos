package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    // Buscar actividades por nombre
    List<Actividad> findByNombreContainingIgnoreCase(String nombre);
    // Buscar una actividad por ID
    Optional<Actividad> findById(Long id);

    // Eliminar todas las relaciones de una actividad en actividad_ninos
    @Modifying
    @Query("DELETE FROM ActividadNinos an WHERE an.actividad.actividadId = :actividadId")
    void deleteAllByActividadId(@Param("actividadId") Long actividadId);

    void deleteById(Long id);
}