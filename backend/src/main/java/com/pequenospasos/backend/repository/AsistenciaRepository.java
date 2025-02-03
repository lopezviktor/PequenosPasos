package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    // Buscar asistencias de un niño específico
    List<Asistencia> findByNinoId(Long ninoId);

    // Buscar asistencias de un educador que haya recibido niños
    List<Asistencia> findByEducadorRecibeId(Long educadorId);

    // Buscar asistencias dentro de un rango de fechas
    List<Asistencia> findByHoraEntradaBetween(LocalDateTime inicio, LocalDateTime fin);
}