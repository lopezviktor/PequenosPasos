package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.ActividadNinos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActividadNinosRepository extends JpaRepository<ActividadNinos, Long> {

    // Buscar actividades en las que participó un niño específico
    List<ActividadNinos> findByNinoId(Long ninoId);

    // Buscar niños que participaron en una actividad específica (Corregido)
    List<ActividadNinos> findByActividad_ActividadId(Long actividadId);

    // Verificar si un niño ya está registrado en una actividad específica (Corregido)
    Optional<ActividadNinos> findByActividad_ActividadIdAndNinoId(Long actividadId, Long ninoId);

    // Eliminar una relación entre actividad y niño (Corregido)
    void deleteByActividad_ActividadIdAndNinoId(Long actividadId, Long ninoId);
}