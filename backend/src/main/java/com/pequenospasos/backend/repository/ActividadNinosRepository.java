package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.ActividadNinos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadNinosRepository extends JpaRepository<ActividadNinos, Long> {

    // Buscar actividades en las que participó un niño
    List<ActividadNinos> findByNinoId(Long ninoId);

    // Buscar niños que participaron en una actividad específica
    List<ActividadNinos> findByActividadId(Long actividadId);
}