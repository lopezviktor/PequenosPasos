package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Higiene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HigieneRepository extends JpaRepository<Higiene, Long> {

    // Buscar registros de higiene de un niño específico
    List<Higiene> findByNinoId(Long ninoId);

    // Buscar registros de higiene realizados por un educador específico
    List<Higiene> findByEducadorId(Long educadorId);

    // Buscar registros de higiene en un rango de fechas
    List<Higiene> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}