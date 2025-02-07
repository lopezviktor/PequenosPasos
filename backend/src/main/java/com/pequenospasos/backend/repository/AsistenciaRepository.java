package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Asistencia;
import com.pequenospasos.backend.entity.Nino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    // Buscar asistencias de un niño específico
    List<Asistencia> findByNino(Nino nino);

    // Buscar asistencias de un educador que haya recibido niños (solo EDUCADORES)
    @Query("SELECT a FROM Asistencia a WHERE a.educadorRecibe.id = :educadorId AND a.educadorRecibe.tipoUsuario = 'EDUCADOR'")
    List<Asistencia> findByEducadorRecibeId(@Param("educadorId") Long educadorId);

    // Buscar asistencias dentro de un rango de fechas
    List<Asistencia> findByHoraEntradaBetween(LocalDateTime inicio, LocalDateTime fin);

    // Buscar asistencias donde un padre haya recogido al niño (solo PADRES)
    @Query("SELECT a FROM Asistencia a WHERE a.padreRecoge.id = :padreId AND a.padreRecoge.tipoUsuario = 'PADRE'")
    List<Asistencia> findByPadreRecogeId(@Param("padreId") Long padreId);

    // Buscar asistencias donde un padre haya entregado al niño (solo PADRES)
    @Query("SELECT a FROM Asistencia a WHERE a.padreEntrega.id = :padreId AND a.padreEntrega.tipoUsuario = 'PADRE'")
    List<Asistencia> findByPadreEntregaId(@Param("padreId") Long padreId);
}