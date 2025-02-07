package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Higiene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HigieneRepository extends JpaRepository<Higiene, Long> {

    // Buscar registros de higiene de un niño específico
    List<Higiene> findByNinoId(Long ninoId);

    // Buscar registros de higiene realizados por un educador específico (solo EDUCADORES)
    @Query("SELECT h FROM Higiene h WHERE h.educador.id = :educadorId AND h.educador.tipoUsuario = 'EDUCADOR'")
    List<Higiene> findByEducadorId(@Param("educadorId") Long educadorId);

    // Buscar registros de higiene en un rango de fechas
    List<Higiene> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    // Último registro de higiene de un niño
    Higiene findTopByNinoIdOrderByFechaHoraDesc(Long ninoId);
}