package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Actividad;
import com.pequenospasos.backend.entity.ActividadNinos;
import com.pequenospasos.backend.entity.Nino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadNinosRepository extends JpaRepository<ActividadNinos, Long> {

    // Buscar actividades en las que participó un niño
    List<ActividadNinos> findByNino(Nino nino);
    // Buscar niños que participaron en una actividad específica
    List<ActividadNinos> findByActividad(Actividad actividad);}