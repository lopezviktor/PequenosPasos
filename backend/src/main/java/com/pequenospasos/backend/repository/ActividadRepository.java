package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    // Buscar actividades por nombre
    List<Actividad> findByNombreContainingIgnoreCase(String nombre);
    // Buscar una actividad por ID
    Optional<Actividad> findById(Long id);
}